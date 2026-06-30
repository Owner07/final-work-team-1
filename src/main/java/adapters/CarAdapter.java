package adapters;

import com.google.gson.Gson;
import models.cars.create.CarCreateRq;
import models.cars.create.CarCreateRs;
import models.cars.get.CarGetRs;
import models.cars.update.CarUpdateRq;
import models.cars.update.CarUpdateRs;

import static io.restassured.RestAssured.given;

public class CarAdapter extends BaseAdapter {

    static Gson gson = new Gson();

    private static final String ENDPOINT = "/car/";

    public static CarCreateRs createCar(CarCreateRq carCreateRq) {
        return given()
                .spec(specCar)
                .body(gson.toJson(carCreateRq))
                .log().all()
                .when()
                .post(ENDPOINT)
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(CarCreateRs.class);
    }

    public static CarGetRs getCar(int carId) {
        return given()
                .spec(specCar)
                .log().all()
                .when()
                .get(ENDPOINT + carId)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(CarGetRs.class);
    }

    public static CarUpdateRs putCar(CarUpdateRq carUpdateRq, int carId) {
        return given()
                .spec(specCar)
                .body(gson.toJson(carUpdateRq))
                .log().all()
                .when()
                .put(ENDPOINT + carId)
                .then()
                .log().all()
                .statusCode(202)
                .extract()
                .as(CarUpdateRs.class);
    }

    public static void deleteCar(int carId) {
        given()
                .spec(specCar)
                .log().all()
                .when()
                .delete(ENDPOINT + carId)
                .then()
                .log().all()
                .statusCode(204);
    }
}
