package adapters;

import io.restassured.specification.RequestSpecification;
import utils.PropertyReader;

import static io.restassured.RestAssured.given;

public class BaseAdapter {
    protected static final String BASE_API_URL = "http://82.142.167.37:4879";

    protected static final String TOKEN = System.getProperty(
            "api_token",
            PropertyReader.getProperty("api_token")
    );
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
  
  public static RequestSpecification specCar = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri("http://82.142.167.37:4879")
            .setAuth(oauth2(TOKEN))
            .build();
}
