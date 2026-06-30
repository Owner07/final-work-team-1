package ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class ReadUserInfoPage {

    private static final String READ_USER_INFO_URL = "http://82.142.167.37:4881/#/read/userInfo";

    private final SelenideElement userIdInput = $("input[type='number']");
    private final SelenideElement readButton = $$("button").findBy(exactText("Read"));
    private final SelenideElement userTable = $("table.tableUser");

    @Step("Открыть страницу Read user with cars")
    public ReadUserInfoPage openPage() {
        log.info("Open Read user info page: {}", READ_USER_INFO_URL);
        open(READ_USER_INFO_URL);
        return this;
    }

    @Step("Проверить, что страница Read user with cars открыта")
    public ReadUserInfoPage shouldBeOpened() {
        userIdInput.shouldBe(visible, Duration.ofSeconds(15));
        readButton.shouldBe(visible, Duration.ofSeconds(15));
        return this;
    }

    @Step("Ввести id пользователя")
    public ReadUserInfoPage setUserId(Long userId) {
        log.info("Set user id: {}", userId);

        userIdInput
                .shouldBe(visible, Duration.ofSeconds(15))
                .clear();

        userIdInput.setValue(String.valueOf(userId));

        return this;
    }

    @Step("Нажать Read")
    public ReadUserInfoPage clickRead() {
        readButton
                .shouldBe(enabled, Duration.ofSeconds(15))
                .click();

        return this;
    }

    @Step("Проверить, что пользователь отображается на странице")
    public ReadUserInfoPage shouldHaveUser(String userId, String firstName, String secondName) {
        userTable.shouldHave(text(userId), Duration.ofSeconds(15));
        userTable.shouldHave(text(firstName), Duration.ofSeconds(15));
        userTable.shouldHave(text(secondName), Duration.ofSeconds(15));
        return this;
    }
}