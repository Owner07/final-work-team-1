package ui;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.*;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.PropertyReader;
import wrappers.Select;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.assertEquals;

@Log4j2
public class LoginTest extends BaseTest{

    private static final String user = PropertyReader.getProperty("user");
    private static final String password = PropertyReader.getProperty("password");

    @Test
    @Description("Проверка на удачный логин в системе дипломного проекта")
    @Severity(SeverityLevel.CRITICAL)
    public void checkOpenPage() {
        log.info("Login to Start page with creds: user {} and password {}",
                user, password);
        loginPage.open()
                .login(user, password)
                .waitForPageLoaded();
        log.info("Successful login");
        Select.selectByText("Users","Read all");
        assertEquals($(byText("Reload")).getText(), "Reload");
    }

    @DataProvider(name = "Тестовые данные для негативного логина")
    public Object[][] loginData() {
        return new Object[][]{
                {"", user, "Incorrect input data"},
                {password, "", "Incorrect input data"},
                {"test", "test", "Incorrect input data"}
        };
    }

    @Description("Проверка логина с не валидными кредами")
    @Epic("E2E")
    @Feature("Логин в дипломном проекте: негатив")
    @Story("Негативный логин")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("NL")
    @Issue("NL-B")
    @Owner("Вейт Владимир")
    @Test(dataProvider = "Тестовые данные для негативного логина",
            testName = "Негативный логин",
            description = "Ввод невалидных значений с помощью метода датапровайдер")
    public void negativeLogin(String user, String password, String expectedErrorMessage) {
        log.info("Negative login test - user: '{}', expected error: '{}'", user, expectedErrorMessage);
        loginPage.open()
                .login(user, password)
                .getGoButton().click();
        String actualConfirmMessage = Selenide.confirm();
        log.info("Actual confirm message: {}", actualConfirmMessage);
        assertEquals(actualConfirmMessage, expectedErrorMessage,
                "Неверное сообщение в confirm окне");
    }
}
