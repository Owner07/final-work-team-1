package wrappers;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Condition.visible;

public class TableValue {

    public static final String LODGERS_TABLE = "tableLodgers";
    public static final String HOUSES_TABLE = "tableHouse";
    public static final String PARKINGS_TABLE = "tableParkings";

    public static List<String> getAllValues(String tableClass, String columnName) {
        // Ждем появления данных в таблице
        $x(String.format("//table[contains(@class, '%s')]/tbody/tr", tableClass))
                .shouldBe(visible);

        List<String> headers = $$x(String.format("//table[contains(@class, '%s')]//th", tableClass))
                .texts()
                .stream()
                .map(h -> h.replace(":", "").replace("\u00A0", " ").trim())
                .collect(Collectors.toList());

        int columnIndex = headers.indexOf(columnName.trim()) + 1;

        if (columnIndex == 0) return List.of();

        return $$x(String.format("//table[contains(@class, '%s')]/tbody/tr/td[%d]", tableClass, columnIndex))
                .texts();
    }
}