package ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class AllDeletePage extends BasePage {

    private final SelenideElement allDeleteMenuButton = $(byText("All DELETE"));
    private final SelenideElement deleteButton = $(byText("DELETE CAR"));

    @Override
    protected SelenideElement getUniqueElement() {
        return deleteButton;
    }

    public AllDeletePage openAllDeletePage() {
        allDeleteMenuButton.shouldBe(visible, enabled).click();
        return this;
    }
}