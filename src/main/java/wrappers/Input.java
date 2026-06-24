package wrappers;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$x;

public class Input {

    public static void write(String fieldName, String text) {
        $x(String.format("//th[contains(text(), '%s')]/following-sibling::td//input", fieldName)).setValue(text);
    }

    public static void writeLogin(String fieldName, String text) {
        $(String.format("[name='%s']", fieldName)).setValue(text);
    }
    public static void fillByColumnName(String columnName, String value) {
        // Находим колонку по заголовку, затем инпут в этой колонке
        String xpath = "//table//th[contains(text(), '" + columnName + "')]/..//input";
        $(xpath).setValue(value);
    }

    public static void inputRead(String text) {
        // Находим любой input внутри btn-group
        $(".btn-group input").setValue(text);
        // Нажимаем кнопку Read в том же контейнере
        $(".btn-group .tableButton").click();
    }
}
