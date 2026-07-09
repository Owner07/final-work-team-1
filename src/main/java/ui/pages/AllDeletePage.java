package ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ui.pages.base.BasePage;

import static com.codeborne.selenide.Selenide.*;

public class AllDeletePage extends BasePage {

    private static final String ALL_DELETE_URL = "/#/delete/all";
    private final SelenideElement deleteButton = $("button[value='car']");

    @Override
    protected SelenideElement getUniqueElement() {
        return deleteButton;
    }

    @Step("Открытие страницы ALL DELETE")
    public AllDeletePage openAllDeletePage() {
        open(ALL_DELETE_URL);
        return this;
    }

    @Step("Проверка, что страница ALL DELETE открыта")
    public AllDeletePage checkAllDeleteOpened() {
        waitForPageLoaded();
        return this;
    }
}