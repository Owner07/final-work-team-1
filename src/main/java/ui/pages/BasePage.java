package ui.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.TimeoutException;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;

@Log4j2
public abstract class BasePage {

    // Абстрактный метод - каждая страница должна указать свой уникальный элемент
    protected abstract SelenideElement getUniqueElement();

    // Ожидание загрузки страницы
    public BasePage waitForPageLoaded() {
        log.info("Waiting for page to load: {}", this.getClass().getSimpleName());
        try {
            getUniqueElement().shouldBe(visible, enabled);
        } catch (TimeoutException e) {
            log.error("Page {} did not load within timeout", this.getClass().getSimpleName());
            throw new AssertionError("Page did not load: " + this.getClass().getSimpleName(), e);
        }
        return this;
    }

    // Проверка открыта ли страница
    public boolean isPageOpened() {
        try {
            return getUniqueElement().is(visible);
        } catch (Exception e) {
            log.warn("Page is not opened: {}", e.getMessage());
            return false;
        }
    }
}
