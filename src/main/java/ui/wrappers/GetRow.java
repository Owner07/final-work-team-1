package ui.wrappers;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class GetRow {
    public static String getRowIdByIndex(String index) {
        SelenideElement row = $x("(//tr[td[1][normalize-space() = '" + index + "']])[1]");
        String id = row.$x("td[1]").getText();
        return id;
    }
}
