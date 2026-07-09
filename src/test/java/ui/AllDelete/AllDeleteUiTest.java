package ui.AllDelete;

import api.adapters.cars.CarAdapter;
import api.adapters.users.UsersAdapter;
import api.models.cars.create.CarCreateRq;
import api.models.houses.create.HouseCreateRequest;
import api.models.houses.create.HouseCreateResponse;
import api.models.users.create.UserCreateRequest;
import api.models.users.create.UserCreateResponse;
import api.models.users.db.UserDbEntity;
import base.BaseTest;
import db.UsersDbClient;
import io.qameta.allure.*;

import org.testng.Assert;
import org.testng.annotations.Test;
import ui.wrappers.*;
import java.util.Arrays;

import static api.adapters.houses.HouseAdapter.createHouse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AllDeleteUiTest extends BaseTest {
    private final UsersAdapter usersAdapter = new UsersAdapter();
    private final UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    @Description("Успешное удаление пользователя")
    @Epic("E2E")
    @Feature("Удаление пользователя")
    @Story("Удаление пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Богатыренко Лидия")
    public void deleteUser() {
        //Предусловие: создать пользователя с помощью api и получаем id созданного пользователя
        UserCreateRequest request = UserCreateRequest.builder()
                .firstName("ForUI")
                .secondName("AutoTest")
                .age(18)
                .sex("MALE")
                .money(100.0)
                .build();
        UserCreateResponse createdUser = usersAdapter.createUserAndGetDto(request);
        Integer createdUserId = createdUser.getId();
        //заходим на страницу удаления, удаляем пользователя по id и проверяем статус.
        allDeletePage.openAllDeletePage()
                .checkAllDeleteOpened();
        InputDelete.deleteItem("user", createdUserId);
        ButtonDelete.clickButtonDelete("user");
        Assert.assertEquals(GetStatusDelete.getStatus("user"), "Status: 204");
        //Проверяем по id, что в БД такой пользователь отсутствует
        UserDbEntity userFromDb = usersDbClient.getUserById(createdUserId);
        assertThat(userFromDb).isNull();
    }

    @Test
    @Description("Успешное удаление дома")
    @Epic("E2E")
    @Feature("Удаление дома")
    @Story("Удаление дома")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Богатыренко Лидия")
    public void deleteHouse() {
        //предусловие: создаём дом с помощью api
        HouseCreateRequest request = HouseCreateRequest.builder()
                .floorCount(6)
                .parkingPlaces(Arrays.asList(
                        HouseCreateRequest.ParkingPlace.builder()
                                .isCovered(true)
                                .isWarm(true)
                                .placesCount(22)
                                .build()
                ))
                .price(10021)
                .build();
        HouseCreateResponse response = createHouse(request);
        int houseId = response.getId();
        //заходим на страницу удаления и удаляем дом
        allDeletePage.openAllDeletePage()
                .checkAllDeleteOpened();
        InputDelete.deleteItem("house", houseId);
        ButtonDelete.clickButtonDelete("house");
        Assert.assertEquals(GetStatusDelete.getStatus("house"), "Status: 204");
    }

    @Test
    @Description("Успешное удаление автомобиля")
    @Epic("E2E")
    @Feature("Удаление автомобиля")
    @Story("Удаление автомобиля")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Богатыренко Лидия")
    public void deleteCar() {
        //Предусловие: создать авто с помощью api
        var carCreateRequest = CarCreateRq.builder()
                .engineType("Gasoline")
                .mark("Audi")
                .model("A1")
                .price(12948)
                .build();
        var createdCar = CarAdapter.createCar(carCreateRequest);
        var carId = createdCar.id;
        assertThat(carId).isNotNull();
        //заходим на страницу удаления и удаляем авто
        allDeletePage.openAllDeletePage()
                .checkAllDeleteOpened();
        InputDelete.deleteItem("car", carId);
        ButtonDelete.clickButtonDelete("car");
        Assert.assertEquals(GetStatusDelete.getStatus("car"), "Status: 204");
    }
}