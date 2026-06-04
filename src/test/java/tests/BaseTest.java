package tests;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import utils.PropertyReader;
import utils.TestListener;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.time.Duration;

import static com.browserup.bup.mitmproxy.MitmProxyProcessManager.MitmProxyLoggingLevel.alert;
import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.shadowCss;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.assertEquals;

@Log4j2
@Listeners({TestListener.class, AllureTestNg.class})
public class BaseTest {

    protected String user = System.getProperty("email", PropertyReader.getProperty("user"));
    protected  String password = System.getProperty("password", PropertyReader.getProperty("password"));

    // Объявляем страницы по примеру
//    protected LoginPage loginPage;

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true, description = "Настройки для драйвера")
    @Description("Инициализация драйвера + опции")
    @Epic("E2E")
    @Story("Инициализация + опции")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Вейт Владимир")
    public void setUp(@Optional("chrome") String browser, ITestContext iTestContext) {
        log.info("Initialization driver and open browser");
        Configuration.timeout = 3000;
        Configuration.baseUrl = "http://82.142.167.37:4881";
        Configuration.clickViaJs = true;

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--incognito");
//            options.addArguments("--disable-notifications");
//            options.addArguments("--disable-popup-blocking");
//            options.addArguments("--disable-infobars");
            options.addArguments("--start-maximized");
            Configuration.browserCapabilities = options;
        }

        if (browser.equalsIgnoreCase("edge")) {
            EdgeOptions options1 = new EdgeOptions();
//            options1.addArguments("--incognito");
//            options1.addArguments("--disable-notifications");
//            options1.addArguments("--disable-popup-blocking");
//            options1.addArguments("--disable-infobars");
            options1.addArguments("--start-maximized");
            Configuration.browserCapabilities = options1;
        }

        // Создаем объекты страниц по примеру
//        loginPage = new LoginPage();
    }

    @Test
    @Description("Проверка на удачный логин в системе дипломного проекта")
    public void checkOpenPage() {
        log.info("Login to Start page with creds: user {} and password {}", user, password);
        Selenide.open("/");
        $("[name=email]").setValue(user);
        $("[name=password]").setValue(password);
        $(byText("GO")).click();
        Selenide.confirm();
        $("#basic-nav-dropdown").click();
        $(byText("Read all")).click();
        assertEquals($(byText("Reload")).getText(), "Reload");
    }

//    @Description("Выход из драйвера")
//    @Epic("E2E")
//    @Story("Закрытие драйвера")
//    @Severity(SeverityLevel.CRITICAL)
//    @Owner("Вейт Владимир")
//    @AfterMethod(alwaysRun = true, description = "Обязательное закрытие драйвера")
//    public void tearDown(ITestResult result) {
//        log.info("Closed browser");
//        Selenide.closeWebDriver();
//    }
}
