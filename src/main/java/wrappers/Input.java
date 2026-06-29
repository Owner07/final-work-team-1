package wrappers;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class Input {

    public static void write(String fieldName, String text) {
        $x(String.format("//th[contains(text(), '%s')]/following-sibling::td//input", fieldName)).setValue(text);
    }

    public static void writeLogin(String fieldName, String text) {
        $(String.format("[name='%s']", fieldName)).setValue(text);
    }

    public static void writeById(String id, String text) {
        $("#" + id).setValue(text);
    }
}
