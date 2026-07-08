package api.adapters.auth;

import api.adapters.base.BaseAdapter;
import api.models.login.AuthResponse;  // ← исправлен импорт
import io.restassured.http.ContentType;
import utils.PropertyReader;

import static io.restassured.RestAssured.given;

public class LoginAdapter extends BaseAdapter {

    private static final String LOGIN_ENDPOINT = "/login";
    private static final String USER = PropertyReader.getProperty("username");
    private static final String PASSWORD = PropertyReader.getProperty("password");

    public static AuthResponse login() {
        return login(USER, PASSWORD);
    }

    public static AuthResponse login(String username, String password) {
        return given()
                .baseUri(BASE_API_URL)
                .contentType(ContentType.URLENC)
                .formParam("username", username)
                .formParam("password", password)
                .log().all()
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .log().all()
                .spec(ok202)
                .extract()
                .as(AuthResponse.class);
    }
}