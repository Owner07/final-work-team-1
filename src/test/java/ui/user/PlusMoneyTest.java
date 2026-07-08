package ui.user;

import io.qameta.allure.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ui.pages.user.PlusMoneyPage;
import ui.pages.user.UsersPage;
import base.BaseTest;

import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Log4j2
@Epic("PFLB Test-API")
@Feature("Users Money Management")
public class PlusMoneyTest extends BaseTest {

    private final UsersPage usersPage = new UsersPage();
    private final PlusMoneyPage plusMoneyPage = new PlusMoneyPage();
    private final Duration userIdLoadDuration = Duration.ofMillis(300);
    private final Duration addMoneyApplyDuration = Duration.ofMillis(100);

    @DataProvider(name = "moneyData")
    public Object[][] positiveMoneyData() {
        return new Object[][]{
                {new MoneyTestData("0.1", "Позитивный сценарий: пополнение на 0.1", "Status: Successfully pushed, code: 200")},
                {new MoneyTestData("100", "Позитивный сценарий: пополнение на 100", "Status: Successfully pushed, code: 200")},
                {new MoneyTestData("500", "Позитивный сценарий: пополнение на 500", "Status: Successfully pushed, code: 200")},
                {new MoneyTestData("1000", "Позитивный сценарий: пополнение на 1000", "Status: Successfully pushed, code: 200")},
                {new MoneyTestData("999999999", "Позитивный сценарий: пополнение баланса с максимальной суммой", "Status: Successfully pushed, code: 200")},
                {new MoneyTestData("0.000000001", "Позитивный сценарий: пополнение баланса с самой минимальной суммой", "Status: Successfully pushed, code: 200")},
                {new MoneyTestData("-0.000000001", "Негативный сценарий: пополнение баланса с самой минимальной отрицательной суммой", "Status: Incorrect input data")},
                {new MoneyTestData("-0.1", "Негативный сценарий: пополнение на отрицательную сумму -0.1", "Status: Incorrect input data")},
                {new MoneyTestData("-100", "Негативный сценарий: пополнение на отрицательную сумму -100", "Status: Incorrect input data")},
                {new MoneyTestData("-500", "Негативный сценарий: пополнение на отрицательную сумму (большая): -500", "Status: Incorrect input data")},
                {new MoneyTestData("-1", "Негативный сценарий: пополнение на отрицательную сумму (минимальная): -1", "Status: Incorrect input data")},
                {new MoneyTestData("", "Негативный сценарий: пополнение на пустую сумму NULL", "Status: Incorrect input data")},
                {new MoneyTestData("0", "Негативный сценарий: пополнение на нулевую сумму 0", "Status: Incorrect input data")}
        };
    }

    @DataProvider(name = "invalidUserIdData")
    public Object[][] invalidUserIdData() {
        return new Object[][]{
                {new UserTestData("99999999", "Несуществующий ID", "Status: AxiosError: Request failed with status code 404")},
                {new UserTestData("00000000", "Нулевой ID", "Status: Incorrect input data")},
                {new UserTestData("abc", "Невалидный формат ID", "Status: Incorrect input data")},
                {new UserTestData("", "Пустой ID", "Status: Incorrect input data")}
        };
    }

    @Test(dataProvider = "moneyData",
            testName = "Пополнение баланса с суммой {0}",
            description = "Пополнение баланса с различными суммами")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add money to user")
    @TmsLink("PM-003")
    @Issue("PM-B-003")
    @Owner("Команда тестирования")
    public void testAddMoneyWithDataProvider(MoneyTestData testData) {
        log.info("Start testAddMoneyWithDataProvider - {}", testData.getDescription());

        usersPage.openUsersListPage();
        try {
            Thread.sleep(userIdLoadDuration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String userId = usersPage.getFirstUserId();
        assertTrue(userId != null && !userId.isEmpty(), "User ID не должен быть пустым");

        plusMoneyPage.openPage();
        plusMoneyPage.addMoneyToUser(userId, testData.getAmount());

        try {
            Thread.sleep(addMoneyApplyDuration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String statusText = plusMoneyPage.getStatusText();
        assertEquals(testData.getExpectedResult(), statusText,
                String.format("Статус должен содержать '%s' для суммы '%s'", testData.getExpectedResult(), testData.getAmount())
        );

        log.info("Finish testAddMoneyWithDataProvider - {}", testData.getDescription());
    }

    @Test(dataProvider = "invalidUserIdData",
            testName = "Пополнение с невалидным ID: {0}",
            description = "Пополнение баланса с невалидными ID пользователей")
    @Severity(SeverityLevel.NORMAL)
    @Story("Add money to user - negative")
    @TmsLink("PM-006")
    @Issue("PM-B-006")
    @Owner("Команда тестирования")
    public void testAddMoneyWithInvalidUserId(UserTestData testData) {
        log.info("Start testAddMoneyWithInvalidUserId - {}", testData.getDescription());

        plusMoneyPage.openPage();
        try {
            Thread.sleep(userIdLoadDuration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String userId = testData.getUserId();
        plusMoneyPage.addMoneyToUser(userId, "100");

        try {
            Thread.sleep(addMoneyApplyDuration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String statusText = plusMoneyPage.getStatusText();
        assertEquals(testData.getExpectedResult(), statusText,
                String.format("Статус должен содержать '%s' для userID '%s'", testData.getExpectedResult(), testData.getUserId())
        );

        log.info("Статус должен содержать - {}", testData.getDescription());
    }

    @AllArgsConstructor
    @Getter
    public static class MoneyTestData {
        String amount;
        String description;
        String expectedResult;

        @Override
        public String toString() {
            return amount;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class UserTestData {
        String userId;
        String description;
        String expectedResult;

        @Override
        public String toString() {
            return userId;
        }
    }
}