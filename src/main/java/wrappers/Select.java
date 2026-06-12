package wrappers;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class Select {

    public static void selectByText(String dropdownText, String optionText) {
        $(byText(dropdownText)).click();
        $(byText(optionText)).click();
    }
}
