package ui.houses;

import com.github.javafaker.Faker;
import ui.dto.NewHouse;
import io.qameta.allure.*;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;
import base.BaseTest;
import ui.wrappers.*;
import java.util.List;
import static org.testng.Assert.*;


@Log4j2
public class HouseTest extends BaseTest {

    String houseId = "2";

    Faker faker = new Faker();

    NewHouse newHouse = NewHouse.builder()
            .floors(String.valueOf(faker.number().numberBetween(1, 25)))
            .price(String.valueOf(faker.number().numberBetween(500, 520)))
            .hasWarmAnd(String.valueOf(faker.number().numberBetween(1, 11)))
            .hasWarmNot(String.valueOf(faker.number().numberBetween(1, 11)))
            .hasColdBut(String.valueOf(faker.number().numberBetween(1, 11)))
            .hasColdNot(String.valueOf(faker.number().numberBetween(1, 11)))
            .build();

    @Test(priority = 2)
    @Description("Получение статуса успешно по ID дома")
    @Epic("E2E")
    @Feature("Получение статуса дома")
    @Story("Позитивный сценарий дома")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Вейт Владимир")
    public void checkOneHouseStatus() {
        housePage.goToReadToOnePage();
        log.info("Open page Read one by ID houses");
        Input.inputRead(houseId);
        assertEquals(GetStatus.getStatus(), "Status: 200 ok");
        log.info("assert status 200 successfully");
    }

    @Test(priority = 3)
    @Description("Проверка ID дома после введения его в поле инпута")
    @Epic("E2E")
    @Feature("Проверка ID дома")
    @Story("Позитивный сценарий дома")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Вейт Владимир")
    public void checkOneHouses() {
        housePage.goToReadToOnePage();
        log.info("Opened page Read one by ID houses");
        Input.inputRead(houseId);
        List<String> allIds = TableValue.getAllValues(TableValue.HOUSES_TABLE, "ID");
        log.info("All IDs found this: {}", allIds);
        assertFalse(allIds.isEmpty(), "Table is empty or column not found");
        log.info("assert no empty successfully");
        assertTrue(allIds.contains(houseId), "ID " + houseId + "not found. Available IDs: " + allIds);
    }

    @Test(priority = 1)
    @Description("Создание нового жилища и получение его ID")
    @Epic("E2E")
    @Feature("Создание нового дома")
    @Story("Позитивный сценарий дома")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Вейт Владимир")
    public void createNewHouses() {
        housePage.goToCreateNewPage();
        log.info("Creating new house page is opened.");
        housePage.createNewHouses(newHouse);
        log.info("Input credential");
        ButtonPush.clickPush();
        log.info("Вывод:{} ", GetNewIdNumber.getNewIdHouse());
        houseId = GetNewIdNumber.getNewIdHouse();
        assertEquals(GetStatus.getStatus(), "Status: Successfully pushed, code: 201");
        log.info("assert successfully");
    }

    @Test(priority = 4)
    @Description("Проверка нового дома в общем списке")
    @Epic("E2E")
    @Feature("Проверка нового дома")
    @Story("Позитивный сценарий дома")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Вейт Владимир")
    public void checkAllHouses() {
        housePage.goToReadAllPage();
        assertEquals(GetRow.getRowIdByIndex(houseId), houseId);
    }
}
