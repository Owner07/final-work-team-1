package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static wrappers.Input.writeById;
import static wrappers.RadioButton.selectRadioByName;
@Data
@Log4j2
public class CarsPage extends BasePage {

    private final SelenideElement pushToAPI = $(byText("-- PUSH TO API --"));

    @Override
    protected SelenideElement getUniqueElement() {
        String currentUrl = Selenide.webdriver().driver().url();

        if (currentUrl.contains("/read/cars")) {
            sleep(1000);
            return $(byText("Engine Type:"));
        }
        if (currentUrl.contains("/create/cars")) {
            return $(byText("ID will be generated"));
        }
        if (currentUrl.contains("/update/users/buyCar")) {
            return $(byText("User ID:"));
        }

        return $("body");
    }

    public CarsPage openReadAll() {
        log.info("Opening read all page");
        $(byText("Cars")).click();
        $(byText("Read all")).click();
        waitForPageLoaded();
        return this;
    }

    public CarsPage openCreateNew() {
        log.info("Opening create new page");
        $(byText("Cars")).click();
        $(byText("Create new")).click();
        waitForPageLoaded();
        return this;
    }

    public CarsPage openBuyOrSellCar() {
        log.info("Opening buy or sell car page");
        $(byText("Cars")).click();
        $(byText("Buy or sell car")).click();
        waitForPageLoaded();
        return this;
    }
    //добавление новой машины через фейкер
    public void addNewCar(String engine, String mark, String model, String price) {
        sleep(2000);
        writeById("car_engine_type_send", engine);
        sleep(1000);
        writeById("car_mark_send", mark);
        sleep(1000);
        writeById("car_model_send", model);
        sleep(1000);
        writeById("car_price_send", price);
        sleep(1000);
        pushToAPI.click();
        sleep(1000);
    }
    //покупка новой машины
    public void buyCar(String carId) {
        writeById("id_send", "3");
        sleep(1000);
        writeById("car_send", carId);
        sleep(1000);
        selectRadioByName("settleOrEvict", "buyCar");
        sleep(1000);
        pushToAPI.click();
        sleep(1000);
    }
    //сохранение айдишника новой машины для следующих тестов
    public String getCreatedCarId() {
        return $("button.newId.btn.btn-secondary")
                .getText()
                .replace("New car ID: ", "");
    }

    public void invalidBuyCar(String user, String car) {
        writeById("id_send", user);
        sleep(1000);
        writeById("car_send", car);
        sleep(1000);
        selectRadioByName("settleOrEvict", "buyCar");
        sleep(1000);
        pushToAPI.click();
        sleep(1000);
    }
    //сортируем по ID
    public void sortByID() {
        $x("//button[contains(text(), 'ID')]").click();
        sleep(1000);
    }
    //получаем первый айдишник после сортировки по ID
    public String getFirstCarId() {
        return $$("tbody tr td:nth-child(1)").first().getText().trim();
    }
}