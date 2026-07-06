package ui.wrappers;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class GetStatusDelete {
    public static String getStatus(String type) {
        Selenide.sleep(3000);
        String xpath = String.format("//button[@value='%s']//following-sibling::button[contains(@class, 'status')]", type);
        return $(By.xpath(xpath)).getText();
    }
}