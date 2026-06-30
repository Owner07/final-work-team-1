package ui.wrappers;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RadioButton {

    public static void selectRadio(String fieldName, String value) {
        $(String.format("//th[contains(text(), '%s')]/following-sibling::td//input[@value='%s']", fieldName, value))
                .parent()
                .click();
    }

    public static void selectRadioByName(String name, String value) {
        $x(String.format("//input[@name='%s' and @value='%s']", name, value)).click();
    }
}
