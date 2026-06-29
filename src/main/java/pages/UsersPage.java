package pages;

import lombok.extern.log4j.Log4j2;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import models.users.create.UserCreateRequest;

import java.util.List;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

@Log4j2
public class UsersPage extends BasePage {

    private static final String READ_USERS_URL = "http://82.142.167.37:4881/#/read/users";

    // Элементы для меню Users
    private final SelenideElement usersMenu = $(byText("Users"));
    private final SelenideElement readAllMenuItem = $(byText("Read all"));
    private final SelenideElement readUserWithCarsMenuItem = $(byText("Read user with cars"));
    private final SelenideElement createNewMenuItem = $(byText("Create new"));
    private final SelenideElement reloadButton = $(byText("Reload"));

    // Элементы для страницы списка пользователей (Read all)
    private final ElementsCollection userIdCells = $$("table.table tbody tr td:first-child");

    @Override
    protected SelenideElement getUniqueElement() {
        return usersMenu;
    }

    //  Методы навигации по меню Users

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

    // Методы для работы со списком пользователей (Read all)

    @Step("Открыть страницу списка пользователей напрямую")
    public UsersPage openUsersListPage() {
        log.info("Opening Read Users page: {}", READ_USERS_URL);
        open(READ_USERS_URL);
        waitForPageLoaded();
        return this;
    }

    @Step("Получить список всех ID пользователей")
    public List<String> getAllUserIds() {
        log.info("Getting all user IDs from table");
        waitForPageLoaded();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        List<String> ids = userIdCells.texts();
        log.info("Found {} user IDs", ids.size());
        return ids;
    }

    @Step("Получить первый ID пользователя из списка")
    public String getFirstUserId() {
        List<String> ids = getAllUserIds();
        if (ids.isEmpty()) {
            log.warn("No users found in table");
            return null;
        }
        String firstId = ids.get(0);
        log.info("First user ID: {}", firstId);
        return firstId;
    }

    @Step("Получить ID пользователя с индексом {index}")
    public String getUserIdByIndex(int index) {
        List<String> ids = getAllUserIds();
        if (index >= ids.size()) {
            log.warn("Index {} out of bounds. Total users: {}", index, ids.size());
            return null;
        }
        String userId = ids.get(index);
        log.info("User ID at index {}: {}", index, userId);
        return userId;
    }

    @Step("Получить случайный ID пользователя")
    public String getRandomUserId() {
        List<String> ids = getAllUserIds();
        if (ids.isEmpty()) {
            log.warn("No users found in table");
            return null;
        }
        int randomIndex = (int) (Math.random() * ids.size());
        String userId = ids.get(randomIndex);
        log.info("Random user ID: {}", userId);
        return userId;
    }

    @Step("Проверить, что пользователь с ID {userId} существует в списке")
    public boolean isUserExists(String userId) {
        List<String> ids = getAllUserIds();
        boolean exists = ids.contains(userId);
        log.info("User {} exists: {}", userId, exists);
        return exists;
    }

    @Step("Обновить страницу (нажать Reload)")
    public UsersPage clickReload() {
        log.info("Clicking Reload button");
        reloadButton.shouldBe(visible).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    // Методы для создания пользователя

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