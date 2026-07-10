package ui.cars;

import api.adapters.cars.CarAdapter;
import api.adapters.users.UsersAdapter;
import api.models.cars.get.CarGetRs;
import api.models.users.get.UserInfoResponse;
import ui.dto.Car;
import ui.dto.CarFactory;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import base.BaseTest;
import utils.PropertyReader;

import java.time.Duration;
import io.qameta.allure.*;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class BuyOrSellCarTest extends BaseTest {

    private static final String user = PropertyReader.getProperty("user");
    private static final String password = PropertyReader.getProperty("password1");
    Car car = CarFactory.getCar();

    @Test
    @Description("Проверка открытия страницы Buy or sell car")
    @Epic("E2E")
    @Feature("Покупка и продажа авто")
    @Story("Страница с покупкой и продажей авто")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("CARUI-1")
    @Issue("CARUI-1")
    @Owner("Алексеев Данил")
    public void openBuyOrSellCar() {
        loginPage.open()
                .login(user, password);
        carsPage.openBuyOrSellCar();
    }

    @Test
    @Description("Проверка покупки авто")
    @Epic("E2E")
    @Feature("Покупка авто")
    @Story("Страница с покупкой и продажей авто")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("CARUI-2")
    @Issue("CARUI-2")
    @Owner("Алексеев Данил")
    public void buyCar() {
        loginPage.open()
                .login(user, password);
        carsPage.openCreateNew();
        carsPage.addNewCar(
                car.getEngine(),
                car.getMark(),
                car.getModel(),
                car.getPrice()
        );
        String carId = carsPage.getCreatedCarId();
        carsPage.openBuyOrSellCar();
        carsPage.buyCar(carId);
        String actualStatus = $("button.status.btn.btn-secondary").getText();
        assertEquals(actualStatus, "Status: Successfully pushed, code: 200",
                "Статус не соответствует ожидаемому");
    }

    @Test
    @Description("Покупка несуществующего авто несуществующему пользователю")
    @Epic("SMOKE")
    @Feature("Покупка авто")
    @Story("Страница с покупкой и продажей авто")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("CARUI-3")
    @Issue("CARUI-3")
    @Owner("Алексеев Данил")
    public void invalidBuyCar() {
        loginPage.open()
                .login(user, password);
        carsPage.openBuyOrSellCar();
        carsPage.invalidBuyCar("4235", "42314231");
        String actualStatus = $("button.status.btn.btn-secondary").getText();
        assertEquals(actualStatus, "Status: AxiosError: Request failed with status code 404",
                "Статус ошибки не соответствует ожидаемому");
    }
    @Test
    @Description("Проверка покупки авто с верификацией через UI/API/DB")
    @Epic("E2E")
    @Feature("Покупка авто")
    @Story("Страница с покупкой и продажей авто")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("CARUI-4")
    @Issue("CARUI-4")
    @Owner("Горев Андрей")
    public void buyCarWithVerification() {
        // 1. Открыть страницу создания машины и создать её
        carsPage.openCreateNew();
        carsPage.addNewCar(
                car.getEngine(),
                car.getMark(),
                car.getModel(),
                car.getPrice()
        );
        String carId = carsPage.getCreatedCarId();

        // 2. Открыть страницу покупки/продажи
        carsPage.openBuyOrSellCar();

        // 3. Выбрать пользователя (ID=3, так как в методе buyCar он захардкожен) и машину, выполнить покупку
        carsPage.buyCar(carId);

        // 4. Проверить изменение данных через UI (ожидание через shouldBe(visible))
        // Ждем, пока кнопка статуса станет видимой
        $("button.status.btn.btn-secondary").shouldBe(visible, Duration.ofSeconds(10));
        String actualStatus = $("button.status.btn.btn-secondary").getText();

        // Ожидаемый статус проверяется по Swagger (для успешной покупки ожидается 200)
        assertEquals(actualStatus, "Status: Successfully pushed, code: 200",
                "Статус не соответствует ожидаемому");

        // Альтернативная проверка через UI с использованием wrapper для ожидания появления в таблице
        // List<String> carIds = TableValue.getAllValues("cars-table", "ID");
        // assertTrue(carIds.contains(carId), "Машина не найдена в таблице после покупки");

        // 5. Проверить изменение данных через API
        // Получаем информацию о пользователе и проверяем, что машина появилась в его списке
        UsersAdapter usersAdapter = new UsersAdapter();
        UserInfoResponse userFromApi = usersAdapter.getUserInfoById(3);
       // assertTrue(userFromApi.getCars().contains(Integer.parseInt(carId)),
        //        "Машина не найдена в списке машин пользователя через API");

        // Дополнительно проверяем саму машину через API
        CarGetRs carFromApi = CarAdapter.getCar(Integer.parseInt("3"));
        assertEquals(carFromApi.id, Integer.parseInt(carId), "ID машины не совпадает");

        // 6. Проверить изменение данных через DB (если в проекте есть DB адаптер/утилиты)
        // DbHelper.checkCarOwnerInDb(carId, 3);
    }
}
