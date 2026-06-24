package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.util.Assert;
import wrappers.Input;
import wrappers.Select;
import wrappers.TableValue;

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
        Select.selectByText("Hoses","Read all");
        if (!isPageOpened()) {
            log.error("Houses page is not opened. Current URL: {}", Selenide.webdriver().driver().url());
            log.error("Reload button element is not visible on the page");
            throw new IllegalStateException("Login page is not opened. Cannot perform login.");
        }log.info("Houses page is opened.");
        return this;
    }

    public HousePage goToReadToOnePage() {
        log.info("Read to one house page is opened.");
        Select.selectByText("Hoses","Read one by ID");
        return this;
    }

    public HousePage goToCreateNewPage() {
        log.info("Create new house page is opened.");
        Select.selectByText("Hoses","Create new");
        return this;
    }

//    public String readOneHouse(String houseId) {
//        Input.inputRead(houseId);
//        return TableValue.getAllValues(TableValue.HOUSES_TABLE,"ID");
//    }
}
