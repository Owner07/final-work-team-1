package ui.cars;

import dto.Car;
import dto.CarFactory;
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
    public void openBuyOrSellCar() {
        loginPage.open()
                .login(user, password);
        carsPage.openBuyOrSellCar();
    }

    @Test
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
