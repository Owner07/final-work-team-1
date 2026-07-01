package api.adapters.houses;

import api.adapters.base.BaseAdapter;
import api.models.houses.create.HouseCreateRequest;
import api.models.houses.create.HouseCreateResponse;
import api.models.houses.get.HouseGetResponse;
import api.models.houses.update.HouseUpdateRequest;
import api.models.houses.update.HouseUpdateResponse;
import io.restassured.module.jsv.JsonSchemaValidator;

import static io.restassured.RestAssured.given;

public class HouseAdapter extends BaseAdapter {

    private static final String ENDPOINT_HOUSE = "/house/";

    public static HouseCreateResponse createHouse(HouseCreateRequest houseCreateRequest) {
        return given()
                .spec(spec)
                .body(gson.toJson(houseCreateRequest))
                .log().all()
                .when()
                .post()
                .then()
                .body(JsonSchemaValidator
                        .matchesJsonSchemaInClasspath("schemas/houses/HouseCreateResponse.json"))
                .log().all()
                .spec(ok201)
                .extract()
                .as(HouseCreateResponse.class);
    }

    public static HouseGetResponse getHouse(String houseId) {
        return given()
                .spec(spec)
                .log().all()
                .when()
                .get(ENDPOINT_HOUSE + houseId)
                .then()
                .body(JsonSchemaValidator
                        .matchesJsonSchemaInClasspath("schemas/houses/HouseGetSchema.json"))
                .log().all()
                .spec(ok200)
                .extract()
                .as(HouseGetResponse.class);
    }

    public static HouseUpdateResponse updateHouse(String houseId, HouseUpdateRequest houseUpdateRequest) {
        return given()
                .spec(spec)
                .body(gson.toJson(houseUpdateRequest))
                .log().all()
                .when()
                .put(ENDPOINT_HOUSE + houseId)
                .then()
                .body(JsonSchemaValidator
                        .matchesJsonSchemaInClasspath("schemas/houses/HouseUpdateSchema.json"))
                .log().all()
                .spec(ok200)
                .extract()
                .as(HouseUpdateResponse.class);
    }

    public static void deleteHouse(String houseId) {
        given()
                .spec(spec)
                .log().all()
                .when()
                .delete(ENDPOINT_HOUSE + houseId)
                .then()
                .log().all()
                .spec(ok204);
    }
}
