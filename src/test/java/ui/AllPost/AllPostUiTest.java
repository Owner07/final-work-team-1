package ui.AllPost;

import api.adapters.cars.CarAdapter;
import api.adapters.houses.HouseAdapter;
import api.adapters.users.UsersAdapter;
import api.models.cars.db.CarDbEntity;
import api.models.users.create.UserCreateRequest;
import api.models.users.create.UserCreateResponse;
import api.models.users.db.UserDbEntity;
import base.BaseTest;
import com.github.javafaker.Faker;
import db.CarDbClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import ui.dto.Car;
import ui.dto.CarFactory;
import ui.dto.NewHouse;
import ui.pages.house.HousePage;
import ui.wrappers.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.codeborne.selenide.Selenide.switchTo;

public class AllPostUiTest extends BaseTest {

    private final HousePage housePage = new HousePage();
    private final UsersAdapter usersAdapter = new UsersAdapter();
    private final HouseAdapter houseAdapter = new HouseAdapter();
    private final CarAdapter carAdapter = new CarAdapter();
    private final CarDbClient carDbClient = new CarDbClient();

    SoftAssert softAssert = new SoftAssert();
    Car car = CarFactory.getCar();

    Faker faker = new Faker();
    NewHouse newHouse = NewHouse.builder()
            .floors(String.valueOf(faker.number().numberBetween(1, 25)))
            .price(String.valueOf(faker.number().numberBetween(500, 520)))
            .hasWarmAnd(String.valueOf(faker.number().numberBetween(1, 11)))
            .hasWarmNot(String.valueOf(faker.number().numberBetween(1, 11)))
            .hasColdBut(String.valueOf(faker.number().numberBetween(1, 11)))
            .hasColdNot(String.valueOf(faker.number().numberBetween(1, 11)))
            .build();

    @Test
    @Description("Успешное создание пользователя на странице All POST")
    @Feature("Создание пользователя на странице All POST")
    @Story("Создание пользователя на странице All POST")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Богатыренко Лидия")
    public void checkCreateUser() {
        Integer createdUserId = null;
        try {
            allPostPage.openAllPostPage();
            switchTo().window(1);
            //создание пользователя и проверка статуса
            allPostPage.fillUserCreateForm("Caitlin", "Clark", 24, "FEMALE", 100500.99)
                    .clickPushInFormCreateUser();
            softAssert.assertEquals(allPostPage.getStatusCreateUser(), "Status: Successfully pushed, code: 201");
            //проверка, что созданный пользователь существует в БД, и его данные верны.
            createdUserId = Integer.valueOf(allPostPage.getNewUserId());
            UserDbEntity userFromDb = usersDbClient.getUserById(Integer.valueOf(createdUserId));
            softAssert.assertTrue(userFromDb != null);
            softAssert.assertEquals(userFromDb.getFirstName(), "Caitlin");
            softAssert.assertEquals(userFromDb.getSecondName(), "Clark");
            softAssert.assertEquals((Object) userFromDb.getAge(), 24, "Возраст не совпадает");
            softAssert.assertEquals((Object) userFromDb.getSex(), false);
            softAssert.assertEquals(userFromDb.getMoney(), BigDecimal.valueOf(100500.99));
            softAssert.assertAll();
        } finally {
            usersAdapter.deleteUser(createdUserId);
        }
    }

    @Test
    @Description("Успешное добавление денег пользователю")
    @Feature("Успешное добавление денег пользователю")
    @Story("Добавление денег пользователю")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Богатыренко Лидия")
    public void addMoneyToUser() {
        //предусловие: создаём пользователя с помощью api и получаем его id и кол-во денег
        UserCreateRequest request = UserCreateRequest.builder()
                .firstName("Auto5")
                .secondName("Test")
                .age(18)
                .sex("MALE")
                .money(100.0)
                .build();
        Response response = usersAdapter.createUser(request);
        UserCreateResponse createdUser = response.as(UserCreateResponse.class);
        Integer userId = createdUser.getId();
        BigDecimal userMoneyBeforeAdd = BigDecimal.valueOf(createdUser.getMoney());
        //заходим на страницу AllPost к форме добавления денег и заполняем её
        allPostPage.openAllPostPage();
        switchTo().window(1);
        BigDecimal addAmount = BigDecimal.valueOf(12055.01);
        allPostPage.fillAddMoneyToUserForm(userId, addAmount.doubleValue())
                .clickPushInAddMoneyToUserForm();
        //проверяем статус и кол-во денег после пополнения
        softAssert.assertEquals(allPostPage.getStatusAddMoney(), "Status: Successfully pushed, code: 200");
        String actualTextMoneyAfterAdd = allPostPage.getUserMoneyAfterAdd();
        BigDecimal actualUserMoney = new BigDecimal(actualTextMoneyAfterAdd).setScale(2, RoundingMode.HALF_UP);
        BigDecimal expectedUserMoney = userMoneyBeforeAdd.add(addAmount);
        softAssert.assertEquals(actualUserMoney, expectedUserMoney);
        softAssert.assertAll();
    }

    @Test
    @Description("Успешное создание авто на странице All POST")
    @Feature("Создание авто на странице All POST")
    @Story("Создание авто на странице All POST")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Богатыренко Лидия")
    public void checkCreateCar() {
        Integer createdCarId = null;
        try {
            allPostPage.openAllPostPage();
            switchTo().window(1);
            carsPage.addNewCar(car.getEngine(), car.getMark(), car.getModel(), car.getPrice());
            allPostPage.clickPushInFormCreateCar();
            softAssert.assertEquals(allPostPage.getStatusCreateCar(), "Status: Successfully pushed, code: 201");
            createdCarId = Integer.valueOf(allPostPage.getNewCarId());
            //берём сгенерированные данные созданного авто и сравниваем с данными в БД
            String expectedEngineName = car.getEngine();
            String expectedMark = car.getMark();
            String expectedModel = car.getModel();
            BigDecimal expectedPrice = new BigDecimal(car.getPrice());
            CarDbEntity carFromDb = carDbClient.getCarById(createdCarId);
            int expectedEngineTypeId = carDbClient.getEngineTypeIdByName(expectedEngineName);
            softAssert.assertEquals(carFromDb.getEngineTypeId(), expectedEngineTypeId);
            softAssert.assertEquals(carFromDb.getMark(), expectedMark);
            softAssert.assertEquals(carFromDb.getModel(), expectedModel);
            softAssert.assertEquals(carFromDb.getPrice().compareTo(expectedPrice), 0); //игнорируем лишние 0 после запятой
            softAssert.assertEquals(carFromDb.getPersonId(), null);
            softAssert.assertAll();
        } finally {
            carAdapter.deleteCar(createdCarId);
        }
    }

    @Test
    @Description("Успешное создание дома на странице All POST")
    @Epic("E2E")
    @Feature("Создание дома на странице All POST")
    @Story("Создание дома на странице All POST")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Богатыренко Лидия")
    public void checkCreateHouseInAllPostPage() {
        Integer createdHouseId = null;
        try {
            allPostPage.openAllPostPage();
            switchTo().window(1);
            //создаём дом и проверяем статус
            housePage.createNewHouses(newHouse);
            ButtonPush.clickPush();
            softAssert.assertEquals(GetStatus.getStatus(), "Status: Successfully pushed, code: 201");
            //получаем номер созданного дома и проверяем, что такой дом с таким id существует
            createdHouseId = Integer.valueOf(GetNewIdNumber.getNewIdHouse());
            softAssert.assertNotNull(createdHouseId);
            housePage.goToReadAllPage();
            softAssert.assertEquals(GetRow.getRowIdByIndex(String.valueOf(createdHouseId)), String.valueOf(createdHouseId));
            softAssert.assertAll();
        } finally {
            houseAdapter.deleteHouse(String.valueOf(createdHouseId));
        }
    }
}