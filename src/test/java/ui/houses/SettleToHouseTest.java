package ui.houses;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import base.BaseTest;
import utils.PropertyReader;
import api.adapters.users.UsersAdapter;
import api.models.users.get.UserInfoResponse;
import ui.pages.house.SettleToHousePage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import java.time.Duration;

public class SettleToHouseTest extends BaseTest {

    private static final String user = PropertyReader.getProperty("user");
    private static final String password = PropertyReader.getProperty("password1");

    // Инициализация страницы заселения
    SettleToHousePage settleToHousePage = new SettleToHousePage();

    @Test
    @Description("Проверка заселения пользователя в дом с верификацией через UI/API")
    @Epic("E2E")
    @Feature("Заселение в дом")
    @Story("Страница заселения в дом")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("HOUSEUI-1")
    @Issue("HOUSEUI-1")
    @Owner("Вейт Владимир")
    public void settleUserToHouse() {
        loginPage.open();
             //   .login(user, password);

        // 1. Открыть страницу заселения
        settleToHousePage.openSettleToHouse();

        // 2. Выбрать пользователя (ID=1) и дом (ID=2), заселить пользователя
        settleToHousePage.settleUser("1", "2");

        // 3. Проверить изменение данных через UI (ожидание через shouldBe(visible))
        $("button.status.btn.btn-secondary").shouldBe(visible, Duration.ofSeconds(10));
        String actualStatus = $("button.status.btn.btn-secondary").getText();

        // Ожидаемый статус проверяется по Swagger (для успешного заселения ожидается 200)
        assertEquals(actualStatus, "Status: Successfully pushed, code: 200",
                "Статус не соответствует ожидаемому");

        // 4. Проверить, что house_id у пользователя изменился через API
        UserInfoResponse userFromApi = UsersAdapter.getUserInfoById(1);
        assertNotNull(userFromApi.getHouse(), "house_id не должен быть null после заселения");
        assertEquals(userFromApi.getHouse(), Integer.valueOf(2), "house_id не изменился на ожидаемый");
    }
}