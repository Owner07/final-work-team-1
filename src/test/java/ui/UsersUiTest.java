package ui;

import adapters.users.UsersAdapter;
import io.qameta.allure.*;
import lombok.extern.log4j.Log4j2;
import models.users.create.UserCreateRequest;
import models.users.create.UserCreateResponse;
import org.testng.annotations.Test;
import pages.ReadUserInfoPage;
import pages.UsersPage;
import utils.UserTestDataFactory;
import java.util.List;
import java.util.ArrayList;

import static org.testng.Assert.assertFalse;

@Log4j2
@Epic("PFLB Test-API")
@Feature("Users UI")
public class UsersUiTest extends BaseTest {

    private final UsersAdapter usersAdapter = new UsersAdapter();
    private final ReadUserInfoPage readUserInfoPage = new ReadUserInfoPage();
    private final UsersPage usersPage = new UsersPage(); // Добавляем инициализацию страницы

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

    @Test
    @Story("Read all via UI")
    @Severity(SeverityLevel.NORMAL)
    @Description("Открытие страницы Read all и проверка наличия пользователей")
    public void readAllUsersUiTest() {
        log.info("Start readAllUsersUiTest");

        loginPage
                .open()
                .login("user@pflb.ru", "user");

        usersPage
                .openReadAll()
                .checkReadAllOpened();

        List<String> userIds = usersPage.getAllUserIds();
        assertFalse(userIds.isEmpty(), "Список пользователей не должен быть пустым");

        log.info("Finish readAllUsersUiTest");
    }

    @Test
    @Story("Create via UI")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Создание пользователя через UI и проверка его появления в Read all")
    public void createUserViaUiAndCheckInReadAllTest() {
        log.info("Start createUserViaUiAndCheckInReadAllTest");

        loginPage
                .open()
                .login("user@pflb.ru", "user");

        // Получаем список ID до создания
        List<String> idsBefore = new ArrayList<>(usersPage.openReadAll().getAllUserIds());

        UserCreateRequest request = UserTestDataFactory.createValidUser();
        usersPage
                .openCreateNew()
                .createUser(request);

        String newUserId = null;
        try {
            // Ждем, пока пользователь появится в БД и подгрузится в таблицу
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Обновляем страницу и получаем новый список ID
            List<String> idsAfter = usersPage.openReadAll().clickReload().getAllUserIds();

            // Находим новый ID (разница между списками)
            idsAfter.removeAll(idsBefore);
            assertFalse(idsAfter.isEmpty(), "Новый пользователь должен был появиться в списке");

            newUserId = idsAfter.get(0);
        } finally {
            // Удаляем созданного пользователя через API для чистоты тестов
            if (newUserId != null) {
                usersAdapter.deleteUser(Long.parseLong(newUserId))
                        .then()
                        .statusCode(204);
            }
        }

        log.info("Finish createUserViaUiAndCheckInReadAllTest");

    }
}