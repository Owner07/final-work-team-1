package ui.pages.house;

import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import ui.pages.base.BasePage;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static ui.wrappers.Input.writeById;
import static ui.wrappers.RadioButton.selectRadioByName;

@Data
@Log4j2
public class SettleToHousePage extends BasePage {

    private final SelenideElement pushToAPI = $(byText("-- PUSH TO API --"));

    @Override
    protected SelenideElement getUniqueElement() {
        return $(byText("User ID:"));
    }

    public SettleToHousePage openSettleToHouse() {
        log.info("Opening settle to house page");
        $(byText("Houses")).click();
        $(byText("Settle or evict user")).click(); // Убедитесь, что пункт меню называется именно так
        waitForPageLoaded();
        return this;
    }

    public void settleUser(String userId, String houseId) {
        writeById("id_send", userId);
        writeById("house_send", houseId);
        selectRadioByName("settleOrEvict", "settle"); // Проверьте имя радио-кнопки в UI
        pushToAPI.click();
    }
}