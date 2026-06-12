package wrappers;

import static com.codeborne.selenide.Selenide.$;

public class Input {

    public static void write(String fieldName, String text) {
        $(String.format("//th[contains(text(), '%s')]/following-sibling::td//input", fieldName)).setValue(text);
    }
}
