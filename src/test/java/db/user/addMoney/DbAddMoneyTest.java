package db.user.addMoney;

import base.BaseTest;
import db.house.HouseDbConnection;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import ui.pages.user.UsersPage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Map;

@Log4j2
@Epic("PFLB Test-API")
@Feature("Сравнение данных из БД с UI")
public class DbAddMoneyTest extends BaseTest {

    private final UsersPage usersPage = new UsersPage();
    private final Duration userIdLoadDuration = Duration.ofMillis(300);
    private HouseDbConnection dbConnection;

    @BeforeTest
    public void beforeTest() {
        dbConnection = new HouseDbConnection();
        dbConnection.connect();
    }

    @Test
    @DisplayName("Сравнить Money пользователя из БД с UI")
    @Description("Получаем данные из БД, затем переходим на UI и сравниваем")
    public void compareMoneyFromDbAndUi() throws SQLException {
        // 1. Подключение к БД и получение данных
        String dbId = null;
        String dbMoney = null;

        String sql = "SELECT id, money FROM public.person LIMIT 1";
        try (ResultSet rs = dbConnection.select(sql)) {
            if (rs.next()) {
                dbId = String.valueOf(rs.getInt("id"));
                dbMoney = rs.getString("money");
                log.info("DB - ID: {}, Money: {}", dbId, dbMoney);
            }
        }

        // 2. Переход на UI страницу
        usersPage.openUsersListPage();

        try {
            Thread.sleep(userIdLoadDuration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 3. Получение данных с UI
        Map<String, String> uiUserData = usersPage.getRowDataById(dbId);
        String uiId = uiUserData.getOrDefault("ID", "NoIdValue");
        String uiMoney = uiUserData.getOrDefault("Money", "NoMoneyValue");

        // 4. Проверка
        SoftAssert soft =  new SoftAssert();
        soft.assertEquals(uiId, dbId);
        soft.assertEquals(uiMoney, dbMoney);
        soft.assertAll();

        log.info("Test passed: {DB_ID='{}', DB_MONEY='{}'} equals {UI_ID='{}',UI_MONEY='{}'}",
                dbId, dbMoney, uiId, uiMoney);
    }

    @AfterTest
    public void afterTest() {
        dbConnection.close();
    }
}
