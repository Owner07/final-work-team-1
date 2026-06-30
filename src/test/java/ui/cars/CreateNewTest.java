package ui.cars;

import dto.Car;
import dto.CarFactory;
import io.qameta.allure.*;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ui.BaseTest;
import utils.PropertyReader;

import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.assertEquals;

@Log4j2
public class CreateNewTest extends BaseTest {

    private static final String user = PropertyReader.getProperty("user");
    private static final String password = PropertyReader.getProperty("password");
    Car car = CarFactory.getCar();

    @Test
    @Description("Открытие страницы создания нового авто")
    @Epic("E2E")
    @Feature("Страница создания нового авто")
    @Story("Страница с созданием нового авто")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("CARUI-4")
    @Issue("CARUI-4")
    @Owner("Алексеев Данил")
    public void openCreateNew() {
        loginPage.open()
                .login(user, password);
        carsPage.openCreateNew();
    }

    @Test
    @Description("Создание нового авто")
    @Epic("E2E")
    @Feature("Создание нового авто")
    @Story("Страница с созданием нового авто")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("CARUI-5")
    @Issue("CARUI-5")
    @Owner("Алексеев Данил")
    public void addNewCar() {
        loginPage.open()
                .login(user, password);
        carsPage.openCreateNew();
        carsPage.addNewCar(
                car.getEngine(),
                car.getMark(),
                car.getModel(),
                car.getPrice()
        );
        String actualStatus = $("button.status.btn.btn-secondary").getText();
        assertEquals(actualStatus, "Status: Successfully pushed, code: 201",
                "Статус не соответствует ожидаемому");
    }

    @DataProvider(name = "Данные для создания авто, кейсы с пустыми полями")
    public Object[][] loginData() {
        return new Object[][]{
                {"", "mark", "model", "price", "Status: Invalid request data"},
                {"engine", "", "model", "price", "Status: Invalid request data"},
                {"engine", "mark", "", "price", "Status: Invalid request data"},
                {"engine", "mark", "model", "", "Status: Invalid request data"},
        };
    }

    @Description("Создание нового авто с пустым полем, 4 варианта")
    @Epic("E2E")
    @Feature("Создание нового авто с пустым полем")
    @Story("Страница с созданием нового авто")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("CARUI-6")
    @Issue("CARUI-6")
    @Owner("Алексеев Данил")
    @Test(dataProvider = "Данные для создания авто, кейсы с пустыми полями",
            testName = "Пустые поля создания авто",
            description = "Ввод пустых полей с помощью метода датапровайдер")
    public void emptyField(String engine, String mark, String model, String price, String errorMessage) {
        loginPage.open()
                .login(user, password);
        carsPage.openCreateNew();
        carsPage.addNewCar(engine, mark, model, price);
        String actualStatus = $("button.status.btn.btn-secondary").getText();
        assertEquals(actualStatus, errorMessage, "Сообщение об ошибке не соответствует ожидаемому");
    }

    @DataProvider(name = "Данные для создания авто, кейсы с невалидными полями")
    public Object[][] invalidField() {
        return new Object[][]{
                {"123", "Tesla", "Model Y", "342900", "Status: AxiosError: Request failed with status code 400"},
                {"Electro", "123", "Model Y", "342900", "Status: AxiosError: Request failed with status code 400"},
                {"Electro", "Tesla", "123", "342900", "Status: AxiosError: Request failed with status code 400"},
                {"Electro", "Tesla", "Model Y", "Price", "Status: Invalid request data"},
        };
    }

    @Description("Создание нового авто с невалидным полем, 4 варианта")
    @Epic("E2E")
    @Feature("Создание нового авто с невалидным полем")
    @Story("Страница с созданием нового авто")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("CARUI-7")
    @Issue("CARUI-7")
    @Owner("Алексеев Данил")
    @Test(dataProvider = "Данные для создания авто, кейсы с невалидными полями",
            testName = "Невалидные поля создания авто",
            description = "Ввод невалидных полей с помощью метода датапровайдер")
    public void invalidField(String engine, String mark, String model, String price, String errorMessage) {
        loginPage.open()
                .login(user, password);
        carsPage.openCreateNew();
        carsPage.addNewCar(engine, mark, model, price);
        String actualStatus = $("button.status.btn.btn-secondary").getText();
        assertEquals(actualStatus, errorMessage, "Сообщение об ошибке не соответствует ожидаемому");
    }
}
