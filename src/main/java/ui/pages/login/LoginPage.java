package ui.pages.login;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import ui.pages.base.BasePage;
import ui.wrappers.Input;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Data
@Log4j2
public class LoginPage extends BasePage {

    private final SelenideElement goButton = $(byText("GO"));
    private final SelenideElement authorizationTitle = $(byText("Authorization"));

    @Override
    protected SelenideElement getUniqueElement() {
        return authorizationTitle;
    }

    @Step("Open login page")
    public LoginPage open() {
        log.info("Opening login page");
        Selenide.open("/");
        waitForPageLoaded();
        return this;
    }

    @Step("Login with user: {user} and password: ****")
    public LoginPage login(String user, String password) {
        // Проверяем, что страница логина открыта
        if (!isPageOpened()) {
            log.error("Login page is not opened. Current URL: {}", Selenide.webdriver().driver().url());
            log.error("Authorization element is not visible on the page");
            throw new IllegalStateException("Login page is not opened. Cannot perform login.");
        }
        log.info("Logging in with user: {}", user);
        Input.writeLogin("email", user);
        Input.writeLogin("password", password);
        goButton.click();
        Selenide.confirm();
        return this;
    }
}
