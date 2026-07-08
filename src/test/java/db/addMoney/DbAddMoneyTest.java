package db.addMoney;

import base.BaseTest;
import db.DbConnection;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import props.AddMoneyProps;
import ui.pages.user.UsersPage;
import utils.AddMoneyUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Epic("PFLB Test-API")
@Feature("Сравнение данных из БД с UI")
public class DbAddMoneyTest extends BaseTest {

    private final UsersPage usersPage = new UsersPage();
    private DbConnection dbConnection;
    private final AddMoneyProps props = new AddMoneyProps();
    private final AtomicReference<Map<String, String>> cachedUserData = new AtomicReference<>();

    @BeforeTest
    public void beforeTest() {
        dbConnection = new DbConnection();
        dbConnection.connect();
    }

    @Test
    @DisplayName("Сравнить Money пользователя из БД с UI")
    @Description("Получаем данные из БД, затем переходим на UI и сравниваем")
    public void compareMoneyFromDbAndUi() throws SQLException {
        // 1. Подключение к БД и получение данных
        String dbId = null;
        String dbMoney = null;

        String sql = """
                SELECT
                	id,
                	CASE
                        WHEN money - TRUNC(money) = 0 THEN TRUNC(money)::TEXT
                        ELSE TO_CHAR(money, 'FM9999999999999999990.99')
                    END AS money
                FROM public.person;
                """;
        try (ResultSet rs = dbConnection.select(sql)) {
            if (rs.next()) {
                dbId = String.valueOf(rs.getInt("id"));
                dbMoney = rs.getString("money");
                log.info("DB - ID: {}, Money: {}", dbId, dbMoney);
            }
        }

        // 2. Переход на UI страницу
        usersPage.openUsersListPage();

        // 3. Получение данных с UI
        String finalDbId = dbId;
        Map<String, String> uiUserData = AddMoneyUtils.getCached(
                cachedUserData,
                () -> usersPage.getRowDataById(finalDbId),
                props.getUserIdLoadDuration(),
                props.getUserIdLoadRetries());
        String uiId = uiUserData.getOrDefault("ID", "NoIdValue");
        String uiMoney = uiUserData.getOrDefault("Money", "NoMoneyValue");

        // 4. Проверка
        SoftAssert soft = new SoftAssert();
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
