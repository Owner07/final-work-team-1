package ui.cars;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import base.BaseTest;
import utils.PropertyReader;

import static org.testng.Assert.assertEquals;

public class ReadAllTest extends BaseTest {

    private static final String user = PropertyReader.getProperty("user");
    private static final String password = PropertyReader.getProperty("password");

    @Test
    @Description("Открытие страницы со всеми авто")
    @Epic("E2E")
    @Feature("Просмотр авто в общем списке")
    @Story("Страница со всеми существующими авто")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("CARUI-8")
    @Issue("CARUI-8")
    @Owner("Алексеев Данил")
    public void openReadAll() {
        loginPage.open()
                .login(user, password);
        carsPage.openReadAll();
    }

    @Test
    @Description("Проверка сортировки по ID страницы со всеми авто")
    @Epic("E2E")
    @Feature("Сортировка авто в общем списке по ID")
    @Story("Страница со всеми существующими авто")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("CARUI-9")
    @Issue("CARUI-9")
    @Owner("Алексеев Данил")
    public void sortID() {
        loginPage.open()
                .login(user, password);
        carsPage.openReadAll();
        carsPage.sortByID();
        String firstId = carsPage.getFirstCarId();
        assertEquals(firstId, "3", "Первый ID должен быть 3");
    }
}