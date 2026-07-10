package api.users;

import api.adapters.users.UsersAdapter;
import db.user.UsersDbClient;
import io.qameta.allure.*;
import lombok.extern.log4j.Log4j2;
import api.models.users.create.UserCreateRequest;
import api.models.users.create.UserCreateResponse;
import api.models.users.db.UserDbEntity;
import api.models.users.get.UserGetResponse;
import api.models.users.get.UserInfoResponse;
import org.testng.annotations.Test;
import utils.UserTestDataFactory;

import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.*;

@Log4j2
@Epic("PFLB Test-API")
@Feature("Users")
public class UsersApiTest {

    private final UsersAdapter usersAdapter = new UsersAdapter();
    private final UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    @Description("Создание нового пользователя через API POST /user")
    @Epic("API")
    @Feature("API Users")
    @Story("Создание пользователя")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("USERAPI-1")
    @Owner("Кесаева Валерия")
    public void createUserApiTest() {
        log.info("Start createUserApiTest");

        Integer createdUserId = null;

        try {
            UserCreateRequest request = UserTestDataFactory.createValidUser();

            UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(request);
            createdUserId = createdUser.getId();

            assertNotNull(createdUser.getId(), "ID созданного пользователя не должен быть null");
            assertEquals(createdUser.getFirstName(), request.getFirstName(), "firstName не совпал");
            assertEquals(createdUser.getSecondName(), request.getSecondName(), "secondName не совпал");
            assertEquals(createdUser.getAge(), request.getAge(), "age не совпал");
            assertEquals(createdUser.getSex(), request.getSex(), "sex не совпал");
            assertEquals(createdUser.getMoney(), request.getMoney(), "money не совпал");

        } finally {
            deleteUserIfCreated(createdUserId);
        }

        log.info("Finish createUserApiTest");
    }

    @Test
    @Description("Получение списка пользователей через API GET /users и проверка наличия созданного пользователя")
    @Epic("API")
    @Feature("API Users")
    @Story("Получение списка пользователей")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("USERAPI-2")
    @Owner("Кесаева Валерия")
    public void createdUserShouldBeInUsersListTest() {
        log.info("Start createdUserShouldBeInUsersListTest");

        Integer createdUserId = null;

        try {
            UserCreateRequest request = UserTestDataFactory.createValidUser();

            UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(request);
            createdUserId = createdUser.getId();

            List<UserGetResponse> users = usersAdapter.getAllUsers()
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList("", UserGetResponse.class);

            assertFalse(users.isEmpty(), "Список пользователей не должен быть пустым");

            final Integer userId = createdUserId;

            boolean userExists = users.stream()
                    .anyMatch(user -> userId.equals(user.getId()));

            assertTrue(userExists, "Созданный пользователь должен быть в списке GET /users");

        } finally {
            deleteUserIfCreated(createdUserId);
        }

        log.info("Finish createdUserShouldBeInUsersListTest");
    }

    @Test
    @Description("Получение созданного пользователя через API GET /user/{userId}")
    @Epic("API")
    @Feature("API Users")
    @Story("Получение пользователя по ID")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("USERAPI-3")
    @Owner("Кесаева Валерия")
    public void createUserAndGetByIdTest() {
        log.info("Start createUserAndGetByIdTest");

        Integer createdUserId = null;

        try {
            UserCreateRequest request = UserTestDataFactory.createValidUser();

            UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(request);
            createdUserId = createdUser.getId();

            UserGetResponse actualUser = usersAdapter.getUserById(createdUserId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(UserGetResponse.class);

            assertEquals(actualUser.getId(), createdUserId, "id не совпал");
            assertEquals(actualUser.getFirstName(), request.getFirstName(), "firstName не совпал");
            assertEquals(actualUser.getSecondName(), request.getSecondName(), "secondName не совпал");
            assertEquals(actualUser.getAge(), request.getAge(), "age не совпал");
            assertEquals(actualUser.getSex(), request.getSex(), "sex не совпал");
            assertEquals(actualUser.getMoney(), request.getMoney(), "money не совпал");

        } finally {
            deleteUserIfCreated(createdUserId);
        }

        log.info("Finish createUserAndGetByIdTest");
    }

    @Test
    @Description("Получение созданного пользователя с машинами через API GET /user/{userId}/info")
    @Epic("API")
    @Feature("API Users")
    @Story("Получение пользователя с машинами")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("USERAPI-4")
    @Owner("Кесаева Валерия")
    public void getUserInfoWithCarsTest() {
        log.info("Start getUserInfoWithCarsTest");

        Integer createdUserId = null;

        try {
            UserCreateRequest request = UserTestDataFactory.createValidUser();

            UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(request);
            createdUserId = createdUser.getId();

            UserInfoResponse actualUser = usersAdapter.getUserInfoById(createdUserId);

            assertEquals(actualUser.getId(), createdUserId, "id не совпал");
            assertEquals(actualUser.getFirstName(), request.getFirstName(), "firstName не совпал");
            assertEquals(actualUser.getSecondName(), request.getSecondName(), "secondName не совпал");
            assertEquals(actualUser.getAge(), request.getAge(), "age не совпал");
            assertEquals(actualUser.getSex(), request.getSex(), "sex не совпал");
            assertEquals(actualUser.getMoney(), request.getMoney(), "money не совпал");
            assertNotNull(actualUser.getCars(), "Поле cars должно присутствовать");

        } finally {
            deleteUserIfCreated(createdUserId);
        }

        log.info("Finish getUserInfoWithCarsTest");
    }

    @Test
    @Description("Создание пользователя через API POST /user и проверка записи в таблице person")
    @Epic("API + DB")
    @Feature("API Users")
    @Story("Создание пользователя с проверкой в БД")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("USERDB-1")
    @Owner("Кесаева Валерия")
    public void createUserAndCheckInDbTest() {
        log.info("Start createUserAndCheckInDbTest");

        Integer createdUserId = null;

        try {
            UserCreateRequest request = UserTestDataFactory.createValidUser();

            UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(request);
            createdUserId = createdUser.getId();

            UserDbEntity userFromDb = usersDbClient.getUserById(createdUserId);

            log.info("Created user id: {}", createdUserId);
            log.info("User from DB: {}", userFromDb);

            assertNotNull(userFromDb, "Пользователь должен быть найден в БД");
            assertEquals(userFromDb.getId(), createdUserId, "id в БД не совпал");
            assertEquals(userFromDb.getFirstName(), request.getFirstName(), "first_name в БД не совпал");
            assertEquals(userFromDb.getSecondName(), request.getSecondName(), "second_name в БД не совпал");
            assertEquals(userFromDb.getAge(), request.getAge(), "age в БД не совпал");

            BigDecimal expectedMoney = BigDecimal.valueOf(request.getMoney());
            assertEquals(userFromDb.getMoney().compareTo(expectedMoney), 0, "money в БД не совпал");

        } finally {
            deleteUserIfCreated(createdUserId);
        }

        log.info("Finish createUserAndCheckInDbTest");
    }

    @Test
    @Description("Удаление пользователя через API DELETE /user/{userId}")
    @Epic("API")
    @Feature("API Users")
    @Story("Удаление пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("USERAPI-5")
    @Owner("Кесаева Валерия")
    public void deleteUserApiTest() {
        log.info("Start deleteUserApiTest");

        Integer createdUserId = null;

        try {
            UserCreateRequest request = UserTestDataFactory.createValidUser();

            UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(request);
            createdUserId = createdUser.getId();

            usersAdapter.deleteUser(createdUserId)
                    .then()
                    .statusCode(204);

            usersAdapter.getUserById(createdUserId)
                    .then()
                    .statusCode(204);

            createdUserId = null;

        } finally {
            deleteUserIfCreated(createdUserId);
        }

        log.info("Finish deleteUserApiTest");
    }

    private void deleteUserIfCreated(Integer userId) {
        if (userId != null) {
            usersAdapter.deleteUser(userId)
                    .then()
                    .statusCode(204);
        }
    }
}