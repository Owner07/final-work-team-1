package api.adapters.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.log4j.Log4j2;
import utils.PropertyReader;
import static io.restassured.RestAssured.oauth2;

import static io.restassured.RestAssured.given;

@Log4j2
public class BaseAdapter {
    protected static final String BASE_API_URL = "http://82.142.167.37:4879";

    protected static final String TOKEN = System.getProperty(
            "api_token",
            PropertyReader.getProperty("api_token")
    );

    // Маскируем токен при логировании
    static {
        String maskedToken = TOKEN != null && TOKEN.length() > 10 ?
                TOKEN.substring(0, 10) + "...[MASKED]" :
                "[NULL]";
        log.info("Token initialized: {}", maskedToken);
    }

    public static Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

//обычный GET без токена
    protected RequestSpecification baseRequest() {
        return given()
                .baseUri(BASE_API_URL)
                .accept("application/json");
    }
//запрос с Bearer token
    protected RequestSpecification authorizedRequest() {
        return baseRequest()
                .header("Authorization", "Bearer " + TOKEN);
    }
//запрос с Bearer token + JSON body.
    protected RequestSpecification authorizedJsonRequest() {
        return authorizedRequest()
                .contentType("application/json");
    }

    public static RequestSpecification spec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(BASE_API_URL)
            .setBasePath("/house")
            .addHeader("Authorization", "Bearer " + TOKEN)
            .build();

    public static ResponseSpecification ok200 = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();

    public static RequestSpecification specCar = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(BASE_API_URL)
            .setAuth(oauth2(TOKEN))
            .build();

    public static ResponseSpecification ok204 = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .build();

    public static ResponseSpecification ok201 = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .build();

    public static ResponseSpecification ok202 = new ResponseSpecBuilder()
            .expectStatusCode(202)
            .build();
}
