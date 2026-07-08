package db.houses;

import db.DbConnection;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.Owner;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import java.sql.ResultSet;
import java.sql.SQLException;

@Log4j2
public class HouseTest {

    private static final String HOUSE_ID_KEY = "createdHouseId";
    private static final String FLOOR_COUNT_KEY = "floorCount";
    private static final String PRICE_KEY = "price";
    private static final String UPDATED_FLOOR_COUNT_KEY = "updatedFloorCount";
    private static final String UPDATED_PRICE_KEY = "updatedPrice";

    @Test(groups = {"step2"}, dependsOnGroups = {"step1"})
    @Owner("Вейт Владимир")
    @Description("Проверка, что созданный дом корректно сохраняется в базе данных со всеми полями (ID, количество этажей, цена)")
    @Severity(SeverityLevel.CRITICAL)
    public void checkHouseAfterCreate(ITestContext context) throws SQLException {
        int houseIdInt = (int) context.getAttribute(HOUSE_ID_KEY);
        String houseIdStr = String.valueOf(houseIdInt);
        int expectedFloorCount = (int) context.getAttribute(FLOOR_COUNT_KEY);
        int expectedPrice = (int) context.getAttribute(PRICE_KEY);
        log.info("Checking house in DB after create - ID: {}", houseIdStr);
        DbConnection db = new DbConnection();
        db.connect();
        ResultSet resultSet = db.select(HousesSql.selectHouseById(houseIdStr));

        boolean found = false;
        while (resultSet.next()) {
            found = true;
            Assert.assertEquals(resultSet.getString("id"), houseIdStr, "ID mismatch");
            Assert.assertEquals(resultSet.getInt("floor_count"), expectedFloorCount, "Floor count mismatch");
            Assert.assertEquals(resultSet.getInt("price"), expectedPrice, "Price mismatch");
            log.info("House verified in DB: ID={}, floor_count={}, price={}",
                    houseIdStr, expectedFloorCount, expectedPrice);
        }
        Assert.assertTrue(found, "House not found in database with ID: " + houseIdStr);
        db.close();
    }

    @Test(groups = {"step2"}, dependsOnGroups = {"step1"})
    @Owner("Вейт Владимир")
    @Description("Проверка корректного сохранения количества этажей созданного дома в базе данных")
    @Severity(SeverityLevel.NORMAL)
    public void checkFloorCountAfterCreate(ITestContext context) throws SQLException {
        int houseIdInt = (int) context.getAttribute(HOUSE_ID_KEY);
        String houseIdStr = String.valueOf(houseIdInt);
        int expectedFloorCount = (int) context.getAttribute(FLOOR_COUNT_KEY);
        log.info("Checking floor count in DB after create - ID: {}, expected: {}",
                houseIdStr, expectedFloorCount);
        DbConnection db = new DbConnection();
        db.connect();
        ResultSet resultSet = db.select(HousesSql.selectHouseById(houseIdStr));
        boolean found = false;
        while (resultSet.next()) {
            found = true;
            int actualFloorCount = resultSet.getInt("floor_count");
            Assert.assertEquals(actualFloorCount, expectedFloorCount, "Floor count mismatch");
            log.info("Floor count verified: expected {}, actual {}",
                    expectedFloorCount, actualFloorCount);
        }
        Assert.assertTrue(found, "House not found in database with ID: " + houseIdStr);
        db.close();
    }

    @Test(groups = {"step2"}, dependsOnGroups = {"step1"})
    @Owner("Вейт Владимир")
    @Description("Проверка корректного сохранения цены созданного дома в базе данных")
    @Severity(SeverityLevel.NORMAL)
    public void checkPriceAfterCreate(ITestContext context) throws SQLException {
        int houseIdInt = (int) context.getAttribute(HOUSE_ID_KEY);
        String houseIdStr = String.valueOf(houseIdInt);
        int expectedPrice = (int) context.getAttribute(PRICE_KEY);
        log.info("Checking price in DB after create - ID: {}, expected: {}",
                houseIdStr, expectedPrice);
        DbConnection db = new DbConnection();
        db.connect();
        ResultSet resultSet = db.select(HousesSql.selectHouseById(houseIdStr));
        boolean found = false;
        while (resultSet.next()) {
            found = true;
            int actualPrice = resultSet.getInt("price");
            Assert.assertEquals(actualPrice, expectedPrice, "Price mismatch");
            log.info("Price verified: expected {}, actual {}",
                    expectedPrice, actualPrice);
        }
        Assert.assertTrue(found, "House not found in database with ID: " + houseIdStr);
        db.close();
    }

    @Test(groups = {"step5"}, dependsOnGroups = {"step4"})
    @Owner("Вейт Владимир")
    @Description("Проверка корректного обновления количества этажей дома в базе данных после выполнения операции обновления")
    @Severity(SeverityLevel.CRITICAL)
    public void checkUpdatedFloorCount(ITestContext context) throws SQLException {
        int houseIdInt = (int) context.getAttribute(HOUSE_ID_KEY);
        String houseIdStr = String.valueOf(houseIdInt);
        int expectedFloorCount = (int) context.getAttribute(UPDATED_FLOOR_COUNT_KEY);
        log.info("Checking updated floor count in DB - ID: {}, expected: {}",
                houseIdStr, expectedFloorCount);
        DbConnection db = new DbConnection();
        db.connect();
        ResultSet resultSet = db.select(HousesSql.selectHouseById(houseIdStr));
        boolean found = false;
        while (resultSet.next()) {
            found = true;
            int actualFloorCount = resultSet.getInt("floor_count");
            Assert.assertEquals(actualFloorCount, expectedFloorCount, "Updated floor count mismatch");
            log.info("Updated floor count verified: expected {}, actual {}",
                    expectedFloorCount, actualFloorCount);
        }
        Assert.assertTrue(found, "House not found in database with ID: " + houseIdStr);
        db.close();
    }

    @Test(groups = {"step5"}, dependsOnGroups = {"step4"})
    @Owner("Вейт Владимир")
    @Description("Проверка корректного обновления цены дома в базе данных после выполнения операции обновления")
    @Severity(SeverityLevel.CRITICAL)
    public void checkUpdatedPrice(ITestContext context) throws SQLException {
        int houseIdInt = (int) context.getAttribute(HOUSE_ID_KEY);
        String houseIdStr = String.valueOf(houseIdInt);
        int expectedPrice = (int) context.getAttribute(UPDATED_PRICE_KEY);
        log.info("Checking updated price in DB - ID: {}, expected: {}",
                houseIdStr, expectedPrice);
        DbConnection db = new DbConnection();
        db.connect();
        ResultSet resultSet = db.select(HousesSql.selectHouseById(houseIdStr));
        boolean found = false;
        while (resultSet.next()) {
            found = true;
            int actualPrice = resultSet.getInt("price");
            Assert.assertEquals(actualPrice, expectedPrice, "Updated price mismatch");
            log.info("Updated price verified: expected {}, actual {}",
                    expectedPrice, actualPrice);
        }
        Assert.assertTrue(found, "House not found in database with ID: " + houseIdStr);
        db.close();
    }

    @Test(groups = {"step8"}, dependsOnGroups = {"step7"})
    @Owner("Вейт Владимир")
    @Description("Проверка, что дом был успешно удален из базы данных после выполнения операции удаления")
    @Severity(SeverityLevel.BLOCKER)
    public void checkHouseDeleted(ITestContext context) throws SQLException {
        int houseIdInt = (int) context.getAttribute(HOUSE_ID_KEY);
        String houseIdStr = String.valueOf(houseIdInt);
        log.info("Checking house deleted from DB - ID: {}", houseIdStr);
        DbConnection db = new DbConnection();
        db.connect();
        ResultSet resultSet = db.select(HousesSql.selectHouseById(houseIdStr));

        boolean found = false;
        while (resultSet.next()) {
            found = true;
            break;
        }
        Assert.assertFalse(found, "House should be deleted but found in database with ID: " + houseIdStr);
        log.info("House confirmed deleted from DB: {}", houseIdStr);
        db.close();
    }
}