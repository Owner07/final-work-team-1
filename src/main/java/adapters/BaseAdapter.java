package adapters;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


import static io.restassured.RestAssured.oauth2;

public class BaseAdapter {

    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQHBmbGIucnUiLCJyb2xlcyI6WyJ1c2VyIl0sImlzcyI6Imh0dHA6Ly84Mi4xNDIuMTY3LjM3OjQ4NzkvbG9naW4iLCJleHAiOjE3ODI3NDYwMDF9.VlepRPGktBsIDvC88TLyFLgjrXBl__en1s7pv-fqzDE";

    public static RequestSpecification specCar = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri("http://82.142.167.37:4879")
            .setAuth(oauth2(TOKEN))
            .build();
}
