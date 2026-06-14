    package ui;

    import com.codeborne.selenide.logevents.SelenideLogger;
    import io.qameta.allure.selenide.AllureSelenide;
    import org.testng.ITestResult;
    import pages.LoginPage;
    import utils.ScreenshotListener;
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

    @Log4j2
    @Listeners({AllureTestNg.class, ScreenshotListener.class, TestListener.class})
    public class BaseTest {

       protected LoginPage loginPage;

        @Parameters({"browser"})
        @BeforeMethod(alwaysRun = true, description = "Настройки для драйвера")
        @Description("Инициализация драйвера + опции")
        @Epic("E2E")
        @Story("Инициализация + опции")
        @Severity(SeverityLevel.CRITICAL)
        @Owner("Вейт Владимир")
        public void setUp(@Optional("chrome") String browser, ITestContext iTestContext) {
            log.info("Initialization driver and open browser");
            SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                    .screenshots(true)      // автоматически делать скриншоты
                    .savePageSource(true)   // сохранять HTML
                    .includeSelenideSteps(true)); // логировать шаги
            Configuration.timeout = 3000;
            Configuration.baseUrl = "http://82.142.167.37:4881";
            Configuration.clickViaJs = true;

            if (browser.equalsIgnoreCase("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                Configuration.browserCapabilities = options;
            }
            if (browser.equalsIgnoreCase("edge")) {
                EdgeOptions options1 = new EdgeOptions();
                options1.addArguments("--start-maximized");
                Configuration.browserCapabilities = options1;
            }

            loginPage = new LoginPage();
        }

        @Description("Выход из драйвера")
        @Epic("E2E")
        @Story("Закрытие драйвера")
        @Severity(SeverityLevel.CRITICAL)
        @Owner("Вейт Владимир")
        @AfterMethod(alwaysRun = true, description = "Обязательное закрытие драйвера")
        public void tearDown(ITestResult result) {
            log.info("Closed browser");
            Selenide.closeWebDriver();
        }
    }
