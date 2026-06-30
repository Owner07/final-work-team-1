package ui.cars;

import dto.Car;
import dto.CarFactory;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import ui.BaseTest;
import utils.PropertyReader;

import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.assertEquals;

public class BuyOrSellCarTest extends BaseTest {

    private static final String user = PropertyReader.getProperty("user");
    private static final String password = PropertyReader.getProperty("password");
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
}
