package pages;

import lombok.extern.log4j.Log4j2;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import models.users.create.UserCreateRequest;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

    @Log4j2
    public class UsersPage extends BasePage {

        private final SelenideElement usersMenu = $(byText("Users"));
        private final SelenideElement readAllMenuItem = $(byText("Read all"));
        private final SelenideElement readUserWithCarsMenuItem = $(byText("Read user with cars"));
        private final SelenideElement createNewMenuItem = $(byText("Create new"));
        private final SelenideElement reloadButton = $(byText("Reload"));

        @Override
        protected SelenideElement getUniqueElement() {
            return usersMenu;
        }

        @Step("Открыть Users → Read all")
        public UsersPage openReadAll() {
            log.info("Open Users -> Read all");
            usersMenu.shouldBe(visible, enabled).click();
            readAllMenuItem.shouldBe(visible, enabled).click();
            return this;
        }

        @Step("Открыть Users → Read user with cars")
        public UsersPage openReadUserWithCars() {
            log.info("Open Users -> Read user with cars");
            usersMenu.shouldBe(visible, enabled).click();
            readUserWithCarsMenuItem.shouldBe(visible, enabled).click();
            return this;
        }

        @Step("Открыть Users → Create new")
        public UsersPage openCreateNew() {
            log.info("Open Users -> Create new");
            usersMenu.shouldBe(visible, enabled).click();
            createNewMenuItem.shouldBe(visible, enabled).click();
            return this;
        }

        @Step("Проверить, что страница Read all открыта")
        public UsersPage checkReadAllOpened() {
            reloadButton.shouldBe(visible, enabled);
            return this;
        }

        @Step("Создать пользователя через UI")
        public UsersPage createUser(UserCreateRequest user) {
            log.info("Create user through UI: {}", user);

            $("input[name='firstName']").setValue(user.getFirstName());
            $("input[name='lastName']").setValue(user.getSecondName());
            $("input[name='age']").setValue(String.valueOf(user.getAge()));
            $("input[name='money']").setValue(String.valueOf(user.getMoney()));
            $("select[name='sex']").selectOption(user.getSex());
            $(byText("Push to API")).click();

            return this;
        }
    }
