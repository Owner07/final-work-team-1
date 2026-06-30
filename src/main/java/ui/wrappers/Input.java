package ui.wrappers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$x;

@Log4j2
public class Input {

    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    public static void writeLogin(String fieldName, String text) {
        $(String.format("[name='%s']", fieldName)).setValue(text);
    }

    public static void writeById(String id, String text) {
        $("#" + id).setValue(text);
    }

    public static void fillByColumnName(String columnName, String value) {
        int index = $$x("//thead/tr/th").texts().indexOf(columnName + ":");
        if (index == -1) {
            index = $$x("//thead/tr/th").texts().indexOf(columnName);
        }
        if (index == -1) {
            throw new RuntimeException("Колонка '" + columnName + "' не найдена");
        }
        $x(String.format("//tbody/tr[1]/td[%d]/input", index + 1))
                .shouldBe(Condition.visible, TIMEOUT)
                .setValue(value);
    }

    public static void fillByColumnNameParking(String columnName, String value) {
        String cleanName = columnName.trim().toLowerCase();
        // Получаем все таблицы на странице
        int tableCount = $$("table").size();
        // Перебираем все таблицы
        for (int tableIndex = 1; tableIndex <= tableCount; tableIndex++) {
            // Получаем ПЕРВЫЙ заголовок таблицы
            String firstHeader = $x(String.format("(//table)[%d]/thead/tr/th[1]", tableIndex))
                    .text()
                    .trim()
                    .replace(":", "")
                    .replace("&nbsp;", " ")
                    .replaceAll("\\s+", " ")
                    .toLowerCase();
            // Проверяем, содержит ли первый заголовок искомый текст
            if (firstHeader.contains(cleanName) || cleanName.contains(firstHeader)) {
                // Нашли нужную таблицу - берем input в первой колонке
                String xpath = String.format("(//table)[%d]/tbody/tr/td[1]/input", tableIndex);
                log.info("Нашли таблицу {} с заголовком: {}", tableIndex, firstHeader);
                log.info("XPath: {}", xpath);

                $x(xpath)
                        .shouldBe(Condition.visible, TIMEOUT)
                        .setValue(value);
                Selenide.sleep(300);
                return;
            }
        }
        throw new RuntimeException("Колонка '" + columnName + "' не найдена ни в одной таблице");
    }

    public static void inputRead(String text) {
        $(".btn-group input").setValue(text);
        $(".btn-group .tableButton").click();
    }
}
