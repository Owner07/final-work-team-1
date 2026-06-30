package api.adapters.cars;

import api.adapters.base.BaseAdapter;
import com.google.gson.Gson;
import api.models.cars.create.CarCreateRq;
import api.models.cars.create.CarCreateRs;
import api.models.cars.get.CarGetRs;
import api.models.cars.update.CarUpdateRq;
import api.models.cars.update.CarUpdateRs;

import static io.restassured.RestAssured.given;

public class CarAdapter extends BaseAdapter {

    static Gson gson = new Gson();

    private static final String ENDPOINT = "/car/";

    public static CarCreateRs createCar(CarCreateRq carCreateRq) {
        return given()
//                .spec(specCar)
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
//                .spec(specCar)
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
//                .spec(specCar)
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
//                .spec(specCar)
                .log().all()
                .when()
                .delete(ENDPOINT + carId)
                .then()
                .log().all()
                .statusCode(204);
    }
}
