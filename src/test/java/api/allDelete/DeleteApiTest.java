package api.allDelete;

import api.adapters.cars.CarAdapter;
import api.adapters.houses.HouseAdapter;
import api.adapters.users.UsersAdapter;
import api.models.cars.create.CarCreateRq;
import api.models.houses.create.HouseCreateRequest;
import api.models.houses.create.HouseCreateResponse;
import api.models.users.create.UserCreateRequest;
import api.models.users.create.UserCreateResponse;
import api.models.users.db.UserDbEntity;
import db.UsersDbClient;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.util.Arrays;

import static api.adapters.houses.HouseAdapter.createHouse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DeleteApiTest {

    private final HouseAdapter houseAdapter = new HouseAdapter();
    private final CarAdapter carAdapter = new CarAdapter();
    private final UsersAdapter usersAdapter = new UsersAdapter();
    private final UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    @Description("Успешное удаление пользователя")
    @Feature("Удаление пользователя")
    @Story("Удаление пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Богатыренко Лидия")
    public void checkDeleteUser() {
        //Предусловие: создать пользователя через API
        UserCreateRequest request = UserCreateRequest.builder()
                .firstName("Auto")
                .secondName("Test")
                .age(18)
                .sex("MALE")
                .money(100.0)
                .build();
        //Получаем id созданного пользователя, удаляем его по id и проверяем, что пользователя больше нет в БД.
        UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(request);
        Integer createdUserId = createdUser.getId();
        usersAdapter.deleteUser(createdUserId)
                .then()
                .statusCode(204);
        UserDbEntity userFromDb = usersDbClient.getUserById(createdUserId);
        assertThat(userFromDb).isNull();
    }

    @Test
    @Description("Успешное удаление дома")
    @Feature("Удаление дома")
    @Story("Удаление дома")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Богатыренко Лидия")
    public void checkDeleteHouse() {
        //Предусловие: создание дома с помощью api и получение его id
        HouseCreateRequest request = HouseCreateRequest.builder()
                .floorCount(5)
                .parkingPlaces(Arrays.asList(
                        HouseCreateRequest.ParkingPlace.builder()
                                .isCovered(true)
                                .isWarm(true)
                                .placesCount(24)
                                .build()
                ))
                .price(10020)
                .build();
        HouseCreateResponse response = createHouse(request);
        int houseId = response.getId();
        //удаление дома по id и проверка статуса
        houseAdapter.deleteHouseWithOutCheckStatus(houseId)
                .then()
                .statusCode(204)
                .log().all();
        //повторное удаление по этому же id и проверка статуса
        houseAdapter.deleteHouseWithOutCheckStatus(houseId)
                .then()
                .statusCode(404)
                .log().all();
    }

    @Test
    @Description("Успешное удаление авто")
    @Feature("Удаление авто")
    @Story("Удаление авто")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Богатыренко Лидия")
    public void checkDeleteCar() {
        //Предусловие: создать авто
        var carCreateRequest = CarCreateRq.builder()
                .engineType("Gasoline")
                .mark("Audi")
                .model("A1")
                .price(12948)
                .build();
        var createdCar = CarAdapter.createCar(carCreateRequest);
        var carId = createdCar.id;
        assertThat(carId).isNotNull();
        //Удаляем созданное авто по id
        carAdapter.deleteCarWithOutCheckStatus(carId)
                .then()
                .statusCode(204);
        carAdapter.deleteCarWithOutCheckStatus(carId)
                .then()
                .statusCode(404);
    }
}