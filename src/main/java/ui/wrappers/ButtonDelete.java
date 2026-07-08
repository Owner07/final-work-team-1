package ui.wrappers;

import static com.codeborne.selenide.Selenide.$;

public class ButtonDelete {
    public static void clickButtonDelete(String value) {
        $(String.format("button[value='%s']", value)).click();
    }
}