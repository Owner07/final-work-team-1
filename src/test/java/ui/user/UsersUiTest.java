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
import db.UsersDbClient;
import api.models.users.db.UserDbEntity;
import ui.pages.UsersPage;
import ui.wrappers.TableValue;

import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.*;

@Log4j2
@Epic("PFLB Test-API")
@Feature("Users UI")
public class UsersUiTest extends BaseTest {

    private final UsersAdapter usersAdapter = new UsersAdapter();
    private final ReadUserInfoPage readUserInfoPage = new ReadUserInfoPage();
    private final UsersPage usersPage = new UsersPage();
    private final UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    @Description("Открытие страницы списка пользователей и проверка загрузки данных после нажатия Reload")
    @Epic("E2E")
    @Feature("Страница списка пользователей")
    @Story("Страница Users → Read all")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("USERUI-1")
    @Owner("Кесаева Валерия")
    public void readAllUsersShouldBeDisplayedAfterReloadTest() {
        log.info("Start readAllUsersShouldBeDisplayedAfterReloadTest");

        usersPage
                .openUsersListPage()
                .checkReadAllOpened()
                .clickReload();

        List<String> ids = TableValue.getAllValues(UsersPage.READ_ALL_USERS_TABLE, "ID");

        assertFalse(ids.isEmpty(), "После клика Reload список пользователей не должен быть пустым");

        assertTrue(
                ids.stream().allMatch(id -> id.matches("\\d+")),
                "Все значения в колонке ID должны быть числами"
        );

        log.info("Finish readAllUsersShouldBeDisplayedAfterReloadTest");
    }

    @Test
    @Description("Открытие страницы пользователя с машинами и проверка отображения созданного пользователя")
    @Epic("E2E")
    @Feature("Страница пользователя с машинами")
    @Story("Страница Users → Read user with cars")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("USERUI-2")
    @Owner("Кесаева Валерия")
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

    @Test
    @Description("Открытие страницы создания нового пользователя и проверка создания пользователя через UI")
    @Epic("E2E")
    @Feature("Страница создания нового пользователя")
    @Story("Страница Users → Create new")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("USERUI-3")
    @Owner("Кесаева Валерия")
    public void userShouldBeCreatedFromCreateNewPageAndSavedInDbTest() {
        log.info("Start userShouldBeCreatedFromCreateNewPageAndSavedInDbTest");

        Long createdUserId = null;

        try {
            UserCreateRequest request = UserTestDataFactory.createValidUser();

            usersPage
                    .openCreateNewPage()
                    .checkCreateNewOpened()
                    .createUser(request)
                    .shouldHaveSuccessCreateStatus();

            createdUserId = usersPage.getCreatedUserId();

            UserCreateResponse userFromApi = usersAdapter.getUserById(createdUserId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(UserCreateResponse.class);

            assertEquals(userFromApi.getId(), createdUserId, "id из API не совпал");
            assertEquals(userFromApi.getFirstName(), request.getFirstName(), "firstName из API не совпал");
            assertEquals(userFromApi.getSecondName(), request.getSecondName(), "secondName из API не совпал");
            assertEquals(userFromApi.getAge(), request.getAge(), "age из API не совпал");
            assertEquals(userFromApi.getSex(), request.getSex(), "sex из API не совпал");
            assertEquals(userFromApi.getMoney(), request.getMoney(), "money из API не совпал");

            UserDbEntity userFromDb = usersDbClient.getUserById(createdUserId);

            assertNotNull(userFromDb, "Пользователь должен быть сохранён в БД");
            assertEquals(userFromDb.getId(), createdUserId, "id в БД не совпал");
            assertEquals(userFromDb.getFirstName(), request.getFirstName(), "first_name в БД не совпал");
            assertEquals(userFromDb.getSecondName(), request.getSecondName(), "second_name в БД не совпал");
            assertEquals(userFromDb.getAge(), request.getAge(), "age в БД не совпал");

            BigDecimal expectedMoney = BigDecimal.valueOf(request.getMoney());
            assertEquals(userFromDb.getMoney().compareTo(expectedMoney), 0, "money в БД не совпал");

        } finally {
            deleteUserIfCreated(createdUserId);
        }

        log.info("Finish userShouldBeCreatedFromCreateNewPageAndSavedInDbTest");
    }
}