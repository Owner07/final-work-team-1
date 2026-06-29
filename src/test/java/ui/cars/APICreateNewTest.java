package ui.cars;

import models.cars.create.CarCreateRq;
import models.cars.create.CarCreateRs;
import models.cars.get.CarGetRs;
import models.cars.update.CarUpdateRq;
import models.cars.update.CarUpdateRs;
import org.testng.annotations.Test;

import static adapters.CarAdapter.*;
import static org.testng.Assert.assertEquals;

public class APICreateNewTest {

    CarCreateRq carCreate = CarCreateRq.builder()
            .engineType("Diesel")
            .mark("Volvo")
            .model("V40")
            .price(540000)
            .build();

    CarUpdateRq carPut = CarUpdateRq.builder()
            .engineType("Electric")
            .mark("Tesla")
            .model("Model Y")
            .price(930500)
            .build();

    @Test
    public void CRUD() {
        //CREATE CAR
        CarCreateRs rs = createCar(carCreate);
        int carId = rs.id;
        assertEquals(rs.engineType, "Diesel");
        assertEquals(rs.mark, "Volvo");
        assertEquals(rs.model, "V40");
        assertEquals(rs.price, 540000);
        //GET CAR
        CarGetRs getRs = getCar(carId);
        assertEquals(getRs.id, carId);
        assertEquals(getRs.engineType, "Diesel");
        assertEquals(getRs.mark, "Volvo");
        assertEquals(getRs.model, "V40");
        assertEquals(getRs.price, 540000);
        //PUT CAR
        CarUpdateRs putRs = putCar(carPut, carId);
        assertEquals(putRs.id, carId);
        assertEquals(putRs.engineType, "Electric");
        assertEquals(putRs.mark, "Tesla");
        assertEquals(putRs.model, "Model Y");
        assertEquals(putRs.price, 930500);
        //DELETE CAR
        deleteCar(carId);
    }
}
