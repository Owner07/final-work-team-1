package ui.wrappers;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class InputDelete {

    public static void deleteItem(String type, Integer value) {
        SelenideElement button = $(String.format("button[value='%s']", type));
        button.closest(".btn-group")
                .$("input[type='number']").setValue(String.valueOf(value));
        button.click();
    }
}