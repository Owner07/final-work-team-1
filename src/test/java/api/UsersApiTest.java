package api;

import adapters.users.UsersAdapter;
import db.UsersDbClient;
import io.qameta.allure.*;
import lombok.extern.log4j.Log4j2;
import models.users.create.UserCreateRequest;
import models.users.create.UserCreateResponse;
import models.users.db.UserDbEntity;
import models.users.get.UserGetResponse;
import models.users.get.UserInfoResponse;
import models.users.update.UserUpdateRequest;
import models.users.update.UserUpdateResponse;
import org.testng.annotations.Test;
import utils.UserTestDataFactory;

import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.*;

@Log4j2
@Epic("PFLB Test-API")
@Feature("Users")
public class UsersApiTest extends BaseTest {

    private final UsersAdapter usersAdapter = new UsersAdapter();
    private final UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    @Story("Create new")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Создание пользователя через API POST /user")
    public void createUserApiTest() {
        log.info("Start createUserApiTest");

        Long createdUserId = null;

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
    @Story("Read all")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Созданный пользователь отображается в списке GET /users")
    public void createdUserShouldBeInUsersListTest() {
        log.info("Start createdUserShouldBeInUsersListTest");

        Long createdUserId = null;

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

            final Long userId = createdUserId;

            boolean userExists = users.stream()
                    .anyMatch(user -> userId.equals(user.getId()));

            assertTrue(userExists, "Созданный пользователь должен быть в списке GET /users");

        } finally {
            deleteUserIfCreated(createdUserId);
        }

        log.info("Finish createdUserShouldBeInUsersListTest");
    }

    @Test
    @Story("Read user by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Созданный пользователь получается через GET /user/{userId}")
    public void createUserAndGetByIdTest() {
        log.info("Start createUserAndGetByIdTest");

        Long createdUserId = null;

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
    @Story("Read user with cars")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Созданный пользователь получается через GET /user/{userId}/info")
    public void getUserInfoWithCarsTest() {
        log.info("Start getUserInfoWithCarsTest");

        Long createdUserId = null;

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
    @Story("Create new with DB assert")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Создание пользователя через API с проверкой записи в БД")
    public void createUserAndCheckInDbTest() {
        log.info("Start createUserAndCheckInDbTest");

        Long createdUserId = null;

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

    private void deleteUserIfCreated(Long userId) {
        if (userId != null) {
            usersAdapter.deleteUser(userId)
                    .then()
                    .statusCode(204);
        }
    }
    @Test
    @Story("Update")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Обновление пользователя через API PUT /user/{userId}")
    public void updateUserApiTest() {
        log.info("Start updateUserApiTest");

        Long createdUserId = null;

        try {
            UserCreateRequest createRequest = UserTestDataFactory.createValidUser();
            UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(createRequest);
            createdUserId = createdUser.getId();

            UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                    .id(createdUserId)
                    .firstName("UpdatedFirst")
                    .secondName("UpdatedSecond")
                    .age(30)
                    .sex("MALE")
                    .money(50000.0)
                    .build();

            UserUpdateResponse updatedUser = usersAdapter.updateUserAndGetDto(createdUserId, updateRequest);

            assertEquals(updatedUser.getId(), createdUserId, "id не совпал");
            assertEquals(updatedUser.getFirstName(), "UpdatedFirst", "firstName не совпал");
            assertEquals(updatedUser.getSecondName(), "UpdatedSecond", "secondName не совпал");
            assertEquals(updatedUser.getAge(), 30, "age не совпал");
            assertEquals(updatedUser.getSex(), "MALE", "sex не совпал");
            assertEquals(updatedUser.getMoney(), 50000.0, "money не совпал");

        } finally {
            deleteUserIfCreated(createdUserId);
        }

        log.info("Finish updateUserApiTest");
    }

    @Test
    @Story("Delete")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Удаление пользователя через API DELETE /user/{userId}")
    public void deleteUserApiTest() {
        log.info("Start deleteUserApiTest");

        UserCreateRequest createRequest = UserTestDataFactory.createValidUser();
        UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(createRequest);
        Long createdUserId = createdUser.getId();

        try {
            usersAdapter.deleteUser(createdUserId)
                    .then()
                    .statusCode(204);

            // Проверяем, что пользователь удален (GET должен вернуть 204)
            usersAdapter.getUserById(createdUserId)
                    .then()
                    .statusCode(204);
        } finally {
            // На случай, если тест упадет до удаления, пытаемся удалить еще раз
            usersAdapter.deleteUser(createdUserId);
        }

        log.info("Finish deleteUserApiTest");
    }
}