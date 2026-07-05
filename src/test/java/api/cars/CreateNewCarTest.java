package api.cars;

import io.qameta.allure.*;
import api.models.cars.create.CarCreateRq;
import api.models.cars.create.CarCreateRs;
import api.models.cars.get.CarGetRs;
import api.models.cars.update.CarUpdateRq;
import api.models.cars.update.CarUpdateRs;
import org.testng.annotations.Test;

import static api.adapters.cars.CarAdapter.*;
import static org.testng.Assert.assertEquals;

public class CreateNewCarTest {

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
    @Description("CRUD авто")
    @Epic("E2E")
    @Feature("Путь жизни авто (создание, чтение, изменение, удаление")
    @Story("Путь жизни авто")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("CARAPI-1")
    @Issue("CARAPI-1")
    @Owner("Алексеев Данил")
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
