package api.adapters.users;

import api.adapters.base.BaseAdapter;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import api.models.users.create.UserCreateRequest;
import api.models.users.create.UserCreateResponse;
import api.models.users.get.UserInfoResponse;

@Log4j2
public class UsersAdapter extends BaseAdapter {

    private static final String USER = "/user";
    private static final String USERS = "/users";
    private static final String USER_BY_ID = "/user/{userId}";
    private static final String USER_INFO_BY_ID = "/user/{userId}/info";

    @Step("Создать пользователя через API POST /user")
    public Response createUser(UserCreateRequest request) {
        log.info("POST {} body={}", USER, request);

        return authorizedJsonRequest()
                .body(request)
                .when()
                .post(USER)
                .then()
                .extract()
                .response();
    }

    @Step("Создать пользователя через API и получить DTO")
    public UserCreateResponse createUserAndGetDto(UserCreateRequest request) {
        return createUser(request)
                .then()
                .statusCode(201)
                .extract()
                .as(UserCreateResponse.class);
    }

    @Step("Получить список пользователей GET /users")
    public Response getAllUsers() {
        log.info("GET {}", USERS);

        return baseRequest()
                .when()
                .get(USERS)
                .then()
                .extract()
                .response();
    }

    @Step("Получить пользователя GET /user/{userId}")
    public Response getUserById(Long userId) {
        log.info("GET {} userId={}", USER_BY_ID, userId);

        return baseRequest()
                .pathParam("userId", userId)
                .when()
                .get(USER_BY_ID)
                .then()
                .extract()
                .response();
    }

    @Step("Получить пользователя с машинами GET /user/{userId}/info")
    public UserInfoResponse getUserInfoById(Long userId) {
        log.info("GET {} userId={}", USER_INFO_BY_ID, userId);

        return baseRequest()
                .pathParam("userId", userId)
                .when()
                .get(USER_INFO_BY_ID)
                .then()
                .statusCode(200)
                .extract()
                .as(UserInfoResponse.class);
    }

    @Step("Удалить пользователя DELETE /user/{userId}")
    public Response deleteUser(Long userId) {
        log.info("DELETE {} userId={}", USER_BY_ID, userId);

        return authorizedRequest()
                .pathParam("userId", userId)
                .when()
                .delete(USER_BY_ID)
                .then()
                .extract()
                .response();
    }
}