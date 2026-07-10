package ui.pages.user;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import ui.pages.base.BasePage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Log4j2
public class PlusMoneyPage extends BasePage {

    private static final String PLUS_MONEY_URL = "http://82.142.167.37:4881/#/update/users/plusMoney";

    private final SelenideElement idSend = $("#id_send");
    private final SelenideElement moneySend = $("#money_send");
    private final SelenideElement pushButton = $("button.tableButton.btn-primary");

    private final SelenideElement statusButton = $("button.status.btn-secondary");
    private final SelenideElement moneyResult = $("button.money.btn-secondary");

    @Override
    protected SelenideElement getUniqueElement() {
        return idSend;
    }

    @Step("Открыть страницу пополнения баланса")
    public PlusMoneyPage openPage() {
        log.info("Opening Plus Money page: {}", PLUS_MONEY_URL);
        open(PLUS_MONEY_URL);
        waitForPageLoaded();
        return this;
    }

    @Step("Ввести ID пользователя: {userId}")
    public PlusMoneyPage setUserId(String userId) {
        log.info("Setting user ID: {}", userId);
        idSend.shouldBe(visible).clear();
        idSend.setValue(userId);
        return this;
    }

    @Step("Ввести сумму для пополнения: {amount}")
    public PlusMoneyPage setMoneyAmount(String amount) {
        log.info("Setting money amount: {}", amount);
        moneySend.shouldBe(visible).clear();
        moneySend.setValue(amount);
        return this;
    }

    @Step("Нажать кнопку PUSH TO API")
    public PlusMoneyPage clickPushToApi() {
        log.info("Clicking PUSH TO API button");
        pushButton.shouldBe(visible).click();
        return this;
    }

    @Step("Получить текущий статус операции")
    public String getStatusText() {
        log.info("Getting status text");
        return statusButton.shouldBe(visible).getText();
    }

    @Step("Получить сумму после операции")
    public String getMoneyAfterOperation() {
        log.info("Getting money after operation");
        return moneyResult.shouldBe(visible).getText();
    }

    @Step("Проверить, что сумма изменилась на {expectedAmount}")
    public PlusMoneyPage shouldHaveMoneyAmount(String expectedAmount) {
        log.info("Checking money amount: {}", expectedAmount);
        moneyResult.shouldBe(visible);
        moneyResult.shouldHave(text(expectedAmount));
        return this;
    }

    @Step("Пополнить баланс пользователя {userId} на сумму {amount}")
    public PlusMoneyPage addMoneyToUser(String userId, String amount) {
        log.info("Adding {} money to user {}", amount, userId);
        return setUserId(userId)
                .setMoneyAmount(amount)
                .clickPushToApi();
    }
}