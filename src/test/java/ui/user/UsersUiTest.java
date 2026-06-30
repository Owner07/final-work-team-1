package ui.user;

import api.adapters.users.UsersAdapter;
import io.qameta.allure.*;
import lombok.extern.log4j.Log4j2;
import api.models.users.create.UserCreateRequest;
import api.models.users.create.UserCreateResponse;
import org.testng.annotations.Test;
import ui.pages.ReadUserInfoPage;
import base.BaseTest;
import utils.UserTestDataFactory;

@Log4j2
@Epic("PFLB Test-API")
@Feature("Users UI")
public class UsersUiTest extends BaseTest {

    private final UsersAdapter usersAdapter = new UsersAdapter();
    private final ReadUserInfoPage readUserInfoPage = new ReadUserInfoPage();

    @Test
    @Story("Read user with cars")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Созданный через API пользователь отображается на странице Read user with cars")
    public void createdUserShouldBeDisplayedOnReadUserInfoPageTest() {
        log.info("Start createdUserShouldBeDisplayedOnReadUserInfoPageTest");

        Long createdUserId = null;

        try {
            UserCreateRequest request = UserTestDataFactory.createValidUser();

            UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(request);
            createdUserId = createdUser.getId();

            log.info("Created user id: {}", createdUserId);
            log.info("Created user firstName: {}", createdUser.getFirstName());
            log.info("Created user secondName: {}", createdUser.getSecondName());

            loginPage
                    .open()
                    .login("user@pflb.ru", "user");

            readUserInfoPage
                    .openPage()
                    .shouldBeOpened()
                    .setUserId(createdUserId)
                    .clickRead()
                    .shouldHaveUser(
                            String.valueOf(createdUserId),
                            createdUser.getFirstName(),
                            createdUser.getSecondName()
                    );

        } finally {
            deleteUserIfCreated(createdUserId);
        }

        log.info("Finish createdUserShouldBeDisplayedOnReadUserInfoPageTest");
    }

    private void deleteUserIfCreated(Long userId) {
        if (userId != null) {
            usersAdapter.deleteUser(userId)
                    .then()
                    .statusCode(204);
        }
    }
}