package ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import ui.pages.base.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AllPostPage extends BasePage {

    private static final String ALL_POST_URL = "/#/create/all";
    private final SelenideElement floorInput = $("#floor_send");

    @Override
    protected SelenideElement getUniqueElement() {
        return floorInput;
    }

    @Step("Открытие страницы ALL POST")
    public AllPostPage openAllPostPage() {
        open(ALL_POST_URL);
        return this;
    }

    @Step("Проверка, что страница ALL POST открыта")
    public AllPostPage checkAllPostOpened() {
        waitForPageLoaded();
        return this;
    }

    //Форма(таблица) создания пользователя
    private SelenideElement getUserCreateForm() {
        return $$("table.table")
                .filterBy(Condition.text("Sex:"))
                .get(0)
                .closest("div"); // Поднимаемся до ближайшего div-обёртки, который содержит таблицу и btn-group
    }

    @Step ("Заполнение данных для создания пользователя")
    public AllPostPage fillUserCreateForm(String firstName, String lastName, Integer age, String sex, Double money) {
        getUserCreateForm().$("#first_name_send").shouldBe(Condition.enabled).setValue(firstName);
        getUserCreateForm().$("#last_name_send").shouldBe(Condition.enabled).setValue(lastName);
        getUserCreateForm().$("#age_send").shouldBe(enabled).setValue(String.valueOf(age));
        getUserCreateForm().$("#money_send").shouldBe(enabled).setValue(String.valueOf(money));
        String sexSelector = "input[name='sex_send'][value='" + sex + "']";
        getUserCreateForm().$(By.cssSelector(sexSelector)).setSelected(true);
        return this;
    }

    @Step ("Нажатие на кнопку PUSH TO API в форме создания пользователя")
    public AllPostPage clickPushInFormCreateUser() {
        getUserCreateForm().$("button.tableButton.btn-primary")
                .shouldHave(text("PUSH TO API"))
                .click();
        return this;
    }

    @Step ("Получение статуса при создании пользователя")
    public String getStatusCreateUser() {
        SelenideElement statusElement = getUserCreateForm().$x(".//button[contains(@class, 'status')]");
        statusElement.shouldHave(Condition.text("Successfully pushed"), Duration.ofSeconds(10));
        return statusElement.getText();
    }

    @Step ("Получение Id созданного пользователя")
    public String getNewUserId() {
        // Ждем пока текст кнопки не станет содержать "New user ID:"
        getUserCreateForm().$x(".//button[contains(@class, 'newId')]")
                .shouldHave(Condition.text("New user ID:"), Duration.ofSeconds(10));
        String fullTextNewId = getUserCreateForm().$x(".//button[contains(@class, 'newId')]")
                .getText();
        return fullTextNewId.replace("New user ID: ", "").trim();
    }

    //Форма(таблица) добавления денег пользователю
    private SelenideElement getAddMoneyToUserForm() {
        return $$("table.table")
                .filterBy(Condition.text("Money:"))
                .get(1)
                .closest("div"); // Поднимаемся до ближайшего div-обёртки, который содержит таблицу и btn-group
    }

    @Step ("Заполнение данных для добавления денег пользователя")
    public AllPostPage fillAddMoneyToUserForm(Integer userId, Double money) {
        var form = getAddMoneyToUserForm();
        form.$("#id_send").shouldBe(Condition.enabled).setValue(String.valueOf(userId));
        form.$("#money_send").shouldBe(enabled).setValue(String.valueOf(money));
        return this;
    }

    @Step ("Нажатие на кнопку PUSH TO API в форме добавления денег пользователю")
    public AllPostPage clickPushInAddMoneyToUserForm() {
        var form = getAddMoneyToUserForm();
        form.$("button.tableButton.btn-primary")
                .shouldHave(text("PUSH TO API"))
                .click();
        return this;
    }

    @Step ("Получение статуса при добавлении денег пользователю")
    public String getStatusAddMoney() {
        var form = getAddMoneyToUserForm();
        SelenideElement statusElement = form.$x(".//button[contains(@class, 'status')]");
        statusElement.shouldHave(Condition.text("Successfully pushed"), Duration.ofSeconds(10));
        return statusElement.getText();
    }

    @Step ("Получение общего кол-ва денег пользователя после добавления")
    public String getUserMoneyAfterAdd() {
        var form = getAddMoneyToUserForm();
        SelenideElement amountOfMoney = form.$(".btn-group").$("button.money");
        amountOfMoney.shouldBe(visible, Duration.ofSeconds(10));
        return amountOfMoney.getText();
    }

    //Форма(таблица) создания авто
    private SelenideElement getCreateCarForm() {
        return $$("table.table")
                .filterBy(Condition.text(" Engine Type:"))
                .get(0)
                .closest("div"); // Поднимаемся до ближайшего div-обёртки, который содержит таблицу и btn-group
    }

    @Step ("Нажатие на кнопку PUSH TO API в форме создания авто")
    public AllPostPage clickPushInFormCreateCar() {
        getCreateCarForm().$("button.tableButton.btn-primary")
                .shouldHave(text("PUSH TO API"))
                .click();
        return this;
    }

    @Step ("Получение статуса при создании авто")
    public String getStatusCreateCar() {
        var form = getCreateCarForm();
        SelenideElement statusElement = form.$x(".//button[contains(@class, 'status')]");
        statusElement.shouldHave(Condition.text("Successfully pushed"), Duration.ofSeconds(10));
        return statusElement.getText();
    }

    @Step ("Получение Id созданного авто")
    public String getNewCarId() {
        var form = getCreateCarForm();
        // Ждем пока текст кнопки не станет содержать "New car ID:"
        form.$x(".//button[contains(@class, 'newId')]")
                .shouldHave(Condition.text("New car ID:"), Duration.ofSeconds(10));
        String fullTextNewId = form.$x(".//button[contains(@class, 'newId')]")
                .getText();
        return fullTextNewId.replace("New car ID: ", "").trim();
    }
}