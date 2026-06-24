package wrappers;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RadioNoTitle {

    public static void selectRadioButton(String radioValue) {
        $(String.format("input[name='settleOrEvict'][value='%s']", radioValue))
                .shouldBe(visible)
                .click();
    }
}
