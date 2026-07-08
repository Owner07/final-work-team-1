package ui.pages.user;

import api.models.users.create.UserCreateRequest;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import ui.pages.base.BasePage;
import ui.wrappers.ButtonPush;
import ui.wrappers.GetStatus;
import ui.wrappers.Input;
import ui.wrappers.TableValue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@Log4j2
public class UsersPage extends BasePage {

    private static final String READ_USERS_URL = "/#/read/users";
    private static final String CREATE_USER_URL = "/#/create/user";
    private static final String READ_USER_WITH_CARS_URL = "/#/read/userInfo";

    public static final String READ_ALL_USERS_TABLE = "table";
    public static final String USERS_TABLE = "tableUser";
    public static final String CARS_TABLE = "tableCars";

    // Элементы для меню Users
    private final SelenideElement usersMenu = $(byText("Users"));
    private final SelenideElement readAllMenuItem = $(byText("Read all"));
    private final SelenideElement readUserWithCarsMenuItem = $(byText("Read user with cars"));
    private final SelenideElement createNewMenuItem = $(byText("Create new"));
    private final SelenideElement reloadButton = $(byText("Reload"));
    private final SelenideElement pushButton = $("button.tableButton.btn-primary");
    private final SelenideElement statusButton = $("button.status.btn-secondary");
    private final SelenideElement newIdButton = $("button.newId.btn-secondary");

    // Элементы для страницы списка пользователей (Read all)
    private final ElementsCollection userIdCells = $$("table.table tbody tr td:first-child");

    // Элементы для страницы Read user with cars
    private final SelenideElement userIdInput = $("input[type='number']");
    private final SelenideElement readButton = $(byText("Read"));
    private final SelenideElement userTable = $("table.tableUser");
    private final SelenideElement carsTable = $("table.tableCars");

    // Элементы для страницы Create new
    private final SelenideElement firstNameInput = $("#first_name_send");
    private final SelenideElement lastNameInput = $("#last_name_send");
    private final SelenideElement ageInput = $("#age_send");
    private final SelenideElement moneyInput = $("#money_send");

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

    @Step("Открыть страницу Users → Read user with cars напрямую")
    public UsersPage openReadUserWithCarsPage() {
        log.info("Opening Read user with cars page: {}", READ_USER_WITH_CARS_URL);
        open(READ_USER_WITH_CARS_URL);
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

    @Step("Открыть страницу Users → Create new напрямую")
    public UsersPage openCreateNewPage() {
        log.info("Opening Create user page: {}", CREATE_USER_URL);
        open(CREATE_USER_URL);
        return this;
    }

    @Step("Проверить, что страница Read all открыта")
    public UsersPage checkReadAllOpened() {
        reloadButton.shouldBe(visible, enabled);
        return this;
    }

    @Step("Проверить, что страница Read user with cars открыта")
    public UsersPage checkReadUserWithCarsOpened() {
        userIdInput.shouldBe(visible, enabled);
        readButton.shouldBe(visible, enabled);
        userTable.shouldBe(visible);
        carsTable.shouldBe(visible);
        return this;
    }

    @Step("Проверить, что страница Create new открыта")
    public UsersPage checkCreateNewOpened() {
        firstNameInput.shouldBe(visible, enabled);
        lastNameInput.shouldBe(visible, enabled);
        ageInput.shouldBe(visible, enabled);
        moneyInput.shouldBe(visible, enabled);
        pushButton.shouldBe(visible, enabled);
        statusButton.shouldBe(visible);
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
        $("table.table").shouldBe(visible);
        List<String> ids = userIdCells.texts();
        log.info("Found {} user IDs", ids.size());
        return ids;
    }

    @Step("Получить список значений из таблицы пользователей по колонке {columnName}")
    public List<String> getAllValuesFromReadAllTable(String columnName) {
        log.info("Getting values from Read all table. Column: {}", columnName);
        $("table.table").shouldBe(visible);
        return TableValue.getAllValues(READ_ALL_USERS_TABLE, columnName);
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

    @Step("Получить данные пользователя по ID")
    public Map<String, String> getRowDataById(String userId) {
        log.info("Getting row data for user ID: {}", userId);
        $("table.table").shouldBe(visible);

        // Получаем все заголовки таблицы
        ElementsCollection headers = $$("table.table thead th");
        List<String> headerList = headers.texts().stream()
                .map(header -> header.replaceAll(":$", ""))
                .toList();

        // Ищем строку с нужным ID
        ElementsCollection allRows = $$("table.table tbody tr");

        for (SelenideElement row : allRows) {
            String rowId = row.$("td:first-child").getText();
            if (rowId.equals(userId)) {
                // Нашли нужную строку
                ElementsCollection cells = row.$$("td");
                Map<String, String> rowData = new LinkedHashMap<>();

                for (int i = 0; i < Math.min(headerList.size(), cells.size()); i++) {
                    rowData.put(headerList.get(i), cells.get(i).getText());
                }

                log.info("Row data for ID {}: {}", userId, rowData);
                return rowData;
            }
        }

        log.warn("User with ID {} not found in table", userId);
        return null;
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
        reloadButton.shouldBe(visible, enabled).click();
        $("table.table").shouldBe(visible);
        return this;
    }

    // Методы для страницы Read user with cars

    @Step("Ввести ID пользователя в поле Read user with cars")
    public UsersPage setUserId(String userId) {
        log.info("Set user id: {}", userId);
        userIdInput.shouldBe(visible, enabled).clear();
        userIdInput.setValue(userId);
        return this;
    }

    @Step("Нажать кнопку Read")
    public UsersPage clickRead() {
        log.info("Click Read button");
        readButton.shouldBe(visible, enabled).click();
        return this;
    }

    @Step("Прочитать пользователя по ID {userId}")
    public UsersPage readUserById(Integer userId) {
        log.info("Read user by id: {}", userId);
        Input.inputRead(String.valueOf(userId));
        return this;
    }

    @Step("Проверить, что таблица пользователя отображается")
    public UsersPage shouldHaveUserTable() {
        userTable.shouldBe(visible);
        return this;
    }

    @Step("Проверить, что таблица машин пользователя отображается")
    public UsersPage shouldHaveCarsTable() {
        carsTable.shouldBe(visible);
        return this;
    }

    @Step("Получить значения из таблицы пользователя по колонке {columnName}")
    public List<String> getUserTableValues(String columnName) {
        log.info("Getting values from user table. Column: {}", columnName);
        userTable.shouldBe(visible);
        return TableValue.getAllValues(USERS_TABLE, columnName);
    }

    @Step("Получить значения из таблицы машин пользователя по колонке {columnName}")
    public List<String> getCarsTableValues(String columnName) {
        log.info("Getting values from cars table. Column: {}", columnName);
        carsTable.shouldBe(visible);
        return TableValue.getAllValues(CARS_TABLE, columnName);
    }

    @Step("Заполнить форму создания пользователя")
    public UsersPage fillCreateUserForm(UserCreateRequest user) {
        log.info("Fill create user form: {}", user);

        $("#first_name_send").shouldBe(visible, enabled).setValue(user.getFirstName());
        $("#last_name_send").shouldBe(visible, enabled).setValue(user.getSecondName());
        $("#age_send").shouldBe(visible, enabled).setValue(String.valueOf(user.getAge()));
        $("#money_send").shouldBe(visible, enabled).setValue(String.valueOf(user.getMoney()));

        if ("MALE".equals(user.getSex())) {
            $$("input[type='radio']").get(0).shouldBe(visible, enabled).click();
        } else {
            $$("input[type='radio']").get(1).shouldBe(visible, enabled).click();
        }

        return this;
    }

    @Step("Нажать кнопку PUSH TO API")
    public UsersPage clickPushToApi() {
        log.info("Click PUSH TO API button");
        ButtonPush.clickPush();
        return this;
    }

    // Методы для создания пользователя

    @Step("Создать пользователя через UI")
    public UsersPage createUser(UserCreateRequest user) {
        log.info("Create user through UI: {}", user);

        fillCreateUserForm(user);
        clickPushToApi();

        return this;
    }

    @Step("Получить статус операции")
    public String getStatusText() {
        String status = GetStatus.getStatus();
        log.info("Operation status: {}", status);
        return status;
    }

    @Step("Проверить успешный статус создания пользователя")
    public UsersPage shouldHaveSuccessCreateStatus() {
        statusButton.shouldBe(visible);
        statusButton.shouldHave(com.codeborne.selenide.Condition.text("201"));
        return this;
    }

    @Step("Получить ID пользователя, созданного через UI")
    public Integer getCreatedUserId() {
        String text = newIdButton.shouldBe(visible).getText();
        log.info("Created user id text: {}", text);

        String id = text.replaceAll("\\D+", "");

        if (id.isBlank()) {
            throw new IllegalStateException("Не удалось получить id созданного пользователя из текста: " + text);
        }

        return Integer.parseInt(id);
    }
}