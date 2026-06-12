package wrappers;

import static com.codeborne.selenide.Selenide.$;

public class RadioButton {

    public static void selectRadio(String fieldName, String value) {
        $(String.format("//th[contains(text(), '%s')]/following-sibling::td//input[@value='%s']", fieldName, value)).parent().click();
    }
}
