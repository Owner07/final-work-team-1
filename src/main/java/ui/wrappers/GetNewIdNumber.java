package ui.wrappers;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.Condition;
import java.time.Duration;

public class GetNewIdNumber {
    public static String getNewIdHouse() {
        // Ждем пока текст кнопки не станет содержать "New house ID:"
        $x("//button[contains(@class, 'newId')]")
                .shouldHave(Condition.text("New house ID:"), Duration.ofSeconds(15));

        String fullText = $x("//button[contains(@class, 'newId')]")
                .getText();

        return fullText.replace("New house ID: ", "").trim();
    }
}