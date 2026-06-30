package ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import ui.dto.NewHouse;
import lombok.extern.log4j.Log4j2;
import ui.wrappers.Input;
import ui.wrappers.Select;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Log4j2
public class HousePage extends BasePage {

    private final SelenideElement reloadButton = $(byText("Reload"));

    @Override
    protected SelenideElement getUniqueElement() {
        return reloadButton;
    }

    public  HousePage goToReadAllPage()
    {
        Select.selectByText("Houses","Read all");
        if (!isPageOpened()) {
            log.error("Houses page is not opened. Current URL: {}", Selenide.webdriver().driver().url());
            log.error("Reload button element is not visible on the page");
            throw new IllegalStateException("Login page is not opened. Cannot perform login.");
        }log.info("Houses page is opened.");
        return this;
    }

    public HousePage goToReadToOnePage() {
        log.info("Read to one house page is opened.");
        Select.selectByText("Houses","Read one by ID");
        return this;
    }

    public HousePage goToCreateNewPage() {
        log.info("Create new house page is opened.");
        Select.selectByText("Houses","Create new");
        return this;
    }

    public HousePage createNewHouses(NewHouse newHouse) {
        Select.selectByText("Houses","Create new");
        log.info("Creating new house page is opened.");
        Input.fillByColumnName("Floors:",newHouse.getFloors());
        Input.fillByColumnName("Price:",newHouse.getPrice());
        Input.fillByColumnNameParking("Has warm and covered parking places",newHouse.getHasWarmAnd());
        Input.fillByColumnNameParking("Has warm, not covered parking places", newHouse.getHasWarmNot());
        Input.fillByColumnNameParking("Has cold, but covered parking places", newHouse.getHasColdBut());
        Input.fillByColumnNameParking("Has cold, not covered parking places", newHouse.getHasColdBut());
        return this;
    }
}
