package wrappers;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ButtonPush {

    public static void clickPush() {
        $("button.tableButton.btn-primary")
                .shouldHave(text("PUSH TO API"))
                .click();
    }
}
