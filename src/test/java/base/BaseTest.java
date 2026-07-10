package base;

import api.adapters.users.UsersAdapter;
import com.codeborne.selenide.logevents.SelenideLogger;
import db.cars.CarDbClient;
import db.user.UsersDbClient;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.ITestResult;
import listeners.ScreenshotListener;
import listeners.TestListener;
import services.annotations.NoLogin;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.testng.AllureTestNg;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.ITestContext;
import org.testng.annotations.*;
import ui.pages.AllDeletePage;
import ui.pages.AllPostPage;
import ui.pages.car.CarsPage;
import ui.pages.house.HousePage;
import ui.pages.login.LoginPage;
import ui.pages.user.ReadUserInfoPage;
import ui.pages.user.UsersPage;
import utils.PropertyReader;

import java.lang.reflect.Method;

@Log4j2
@Listeners({AllureTestNg.class, ScreenshotListener.class, TestListener.class})
public class BaseTest {

    private static final String user = System.getProperty("user", PropertyReader.getProperty("user"));
    private static final String password = System.getProperty("password1", PropertyReader.getProperty("password1"));

    protected LoginPage loginPage;
    protected CarsPage carsPage;
    protected HousePage housePage;
    protected UsersAdapter usersAdapter;
    protected UsersPage usersPage;
    protected ReadUserInfoPage readUserInfoPage;
    protected UsersDbClient usersDbClient;
    protected AllDeletePage allDeletePage;
    protected AllPostPage allPostPage;
    protected CarDbClient carDbClient;

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true, description = "Настройки для драйвера")
    public void setUp(@Optional("chrome") String browser, ITestContext iTestContext, Method method) {
        log.info("Initialization driver and open browser for: " + browser);

        if (WebDriverRunner.hasWebDriverStarted()) {
            log.info("Closing existing webdriver before starting new test");
            Selenide.closeWebDriver();
        }

        Configuration.browserCapabilities = null;

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .includeSelenideSteps(true));

        Configuration.timeout = 15000;
        Configuration.baseUrl = "http://82.142.167.37:4881";
        Configuration.clickViaJs = true;
        Configuration.headless = true;

        // Устанавливаем браузер
        Configuration.browser = browser.toLowerCase();

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--remote-allow-origins=*");
            Configuration.browserCapabilities = options;
            log.info("Chrome options configured");
        } else if (browser.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            Configuration.browserCapabilities = options;
            log.info("Edge options configured");
        }

        loginPage = new LoginPage();
        carsPage = new CarsPage();
        housePage = new HousePage();
        usersAdapter = new UsersAdapter();
        usersPage = new UsersPage();
        readUserInfoPage = new ReadUserInfoPage();
        usersDbClient = new UsersDbClient();
        allDeletePage = new AllDeletePage();
        allPostPage = new AllPostPage();
        carDbClient = new CarDbClient();

        // Проверяем, есть ли аннотация @NoLogin на тестовом методе
        boolean noLogin = method.isAnnotationPresent(NoLogin.class);

        if (noLogin) {
            log.info("SKIP LOGIN: Test method '{}' has @NoLogin annotation", method.getName());
        } else {
            // По умолчанию - выполняем логин
            log.info("Login to Start page with creds: user {} and password ****", user);
            loginPage.open()
                    .login(user, password)
                    .waitForPageLoaded();
            log.info("Successful login");
        }
    }

    @AfterMethod(alwaysRun = true, description = "Обязательное закрытие драйвера")
    public void tearDown(ITestResult result) {
        log.info("Closed browser");
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.closeWebDriver();
        }
        Configuration.browserCapabilities = null;
    }
}