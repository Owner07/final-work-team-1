package ui.wrappers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class Select {

    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    public static void selectByText(String dropdownText, String optionText) {
        // Ожидаем, что дропдаун видим и кликабелен
        $(byText(dropdownText))
                .shouldBe(Condition.visible, TIMEOUT)
                .shouldBe(Condition.enabled, TIMEOUT)
                .click();
        Selenide.sleep(300);

        // Ожидаем, что опция видима и кликабельна
        $(byText(optionText))
                .shouldBe(Condition.visible, TIMEOUT)
                .shouldBe(Condition.enabled, TIMEOUT)
                .click();
        Selenide.sleep(300);
    }
}
