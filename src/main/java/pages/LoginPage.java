package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import utils.PropertyReader;
import wrappers.Input;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Data
@Log4j2
public class LoginPage extends BasePage{

    private final SelenideElement goButton = $(byText("GO"));
    private final SelenideElement authorizationTitle = $(byText("Authorization"));

    @Override
    protected SelenideElement getUniqueElement() {
        return authorizationTitle;
    }

    public LoginPage open() {
        log.info("Opening login page");
        Selenide.open("/");
        waitForPageLoaded();
        return this;
    }

    public LoginPage login(String user, String password) {
        log.info("Logging in with user: {}", user);
        Input.writeLogin("email", user);
        Input.writeLogin("password", password);
        goButton.click();
        Selenide.confirm();
        return this;
    }
}
