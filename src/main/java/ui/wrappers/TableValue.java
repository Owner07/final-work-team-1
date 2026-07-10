package ui.wrappers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TableValue {

    public static final String LODGERS_TABLE = "tableLodgers";
    public static final String HOUSES_TABLE = "tableHouse";
    public static final String PARKINGS_TABLE = "tableParkings";

    public static List<String> getAllValues(String tableClass, String columnName) {
        // Формируем точный XPath для таблицы с классом
        String tableXpath = String.format("//table[contains(@class, '%s')]", tableClass);

        Selenide.sleep(1000);
        // Проверяем, сколько таблиц с таким классом
        int tableCount = $$x(tableXpath).size();
        log.info("Найдено таблиц с классом '{}': {}", tableClass, tableCount);
        if (tableCount == 0) {
            System.out.println("Таблица с классом '" + tableClass + "' не найдена!");
            return List.of();
        }
        // Ждем появления таблицы
        $x(tableXpath).shouldBe(Condition.visible, Duration.ofSeconds(10));
        // Ждем появления данных в таблице
        $x(tableXpath + "/tbody/tr").shouldBe(Condition.visible, Duration.ofSeconds(10));
        // Получаем заголовки таблицы
        List<String> headers = $$x(tableXpath + "//th")
                .texts()
                .stream()
                .map(h -> h.replace(":", "").replace("\u00A0", " ").trim())
                .collect(Collectors.toList());
        log.info("Заголовки таблицы '{}': {}", tableClass, headers);
        // Находим индекс нужной колонки
        int columnIndex = headers.indexOf(columnName.trim()) + 1;
        log.info("Индекс колонки '{}': {}", columnName, columnIndex);
        if (columnIndex == 0) {
            System.out.println("Колонка '" + columnName + "' не найдена. Доступные заголовки: " + headers);
            return List.of();
        }
        // Получаем все значения из найденной колонки
        List<String> result = $$x(String.format(tableXpath + "/tbody/tr/td[%d]", columnIndex))
                .texts();
        log.info("Найденные значения в колонке '{}': {}", columnName, result);
        return result;
    }
}
