package api.houses;

import api.adapters.houses.HouseAdapter;
import api.models.houses.create.HouseCreateRequest;
import api.models.houses.create.HouseCreateResponse;
import api.models.houses.create.HouseCreateRequest.ParkingPlace;
import api.models.houses.get.HouseGetResponse;
import api.models.houses.update.HouseUpdateRequest;
import api.models.houses.update.HouseUpdateResponse;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.ArrayList;

import static org.testng.Assert.fail;

@Log4j2
public class HouseTest {

    private static final String HOUSE_ID_KEY = "createdHouseId";
    private static final String FLOOR_COUNT_KEY = "floorCount";
    private static final String PRICE_KEY = "price";
    private static final String UPDATED_FLOOR_COUNT_KEY = "updatedFloorCount";
    private static final String UPDATED_PRICE_KEY = "updatedPrice";

    @Test(groups = {"step1"})
    public void createHouseTest(ITestContext context) {
        SoftAssert softAssert = new SoftAssert();

        int floorCount = (int) (Math.random() * 10) + 5;
        int price = (int) (Math.random() * 9900) + 100;

        log.info("Starting create house test");
        HouseCreateRequest request = HouseCreateRequest.builder()
                .floorCount(floorCount)
                .id(178)
                .parkingPlaces(Arrays.asList(
                        ParkingPlace.builder()
                                .id(10)
                                .isCovered(true)
                                .isWarm(true)
                                .placesCount(24)
                                .build()
                ))
                .price(price)
                .build();

        HouseCreateResponse response = HouseAdapter.createHouse(request);

        softAssert.assertNotNull(response, "Response is null");
        softAssert.assertTrue(response.getId() > 0, "ID should be positive");
        softAssert.assertEquals(response.getFloorCount(), floorCount, "Floor count mismatch");
        softAssert.assertEquals(response.getPrice(), price, "Price mismatch");
        softAssert.assertNotNull(response.getParkingPlaces(), "Parking places should not be null");
        int createdHouseId = response.getId();
        context.setAttribute(HOUSE_ID_KEY, createdHouseId);
        context.setAttribute(FLOOR_COUNT_KEY, floorCount);
        context.setAttribute(PRICE_KEY, price);
        log.info("House created successfully with ID: {}", createdHouseId);
        softAssert.assertAll();
    }

    @Test(groups = {"step3"}, dependsOnGroups = {"step2"})
    public void getHouseTest(ITestContext context) {
        SoftAssert softAssert = new SoftAssert();

        int createdHouseId = (int) context.getAttribute(HOUSE_ID_KEY);
        int floorCount = (int) context.getAttribute(FLOOR_COUNT_KEY);
        int price = (int) context.getAttribute(PRICE_KEY);

        log.info("Starting get house test for ID: {}", createdHouseId);
        HouseGetResponse response = HouseAdapter.getHouse(String.valueOf(createdHouseId));
        softAssert.assertNotNull(response, "Response is null");
        softAssert.assertTrue(response.getId() > 0, "ID should be positive");
        softAssert.assertEquals(response.getFloorCount(), floorCount, "Floor count mismatch");
        softAssert.assertEquals(response.getPrice(), (double) price, "Price mismatch");
        softAssert.assertNotNull(response.getParkingPlaces(), "Parking places should not be null");

        log.info("House retrieved successfully with ID: {}", response.getId());
        softAssert.assertAll();
    }

    @Test(groups = {"step4"}, dependsOnGroups = {"step3"})
    public void updateHouseTest(ITestContext context) {
        SoftAssert softAssert = new SoftAssert();

        int createdHouseId = (int) context.getAttribute(HOUSE_ID_KEY);
        int updatedFloorCount = (int) (Math.random() * 10) + 5;
        int updatedPrice = (int) (Math.random() * 9900) + 100;

        log.info("Starting update house test for ID: {}", createdHouseId);
        HouseUpdateRequest request = HouseUpdateRequest.builder()
                .id(createdHouseId)
                .floorCount(updatedFloorCount)
                .price(updatedPrice)
                .parkingPlaces(Arrays.asList(
                        HouseUpdateRequest.ParkingPlace.builder()
                                .id(0)
                                .isWarm(true)
                                .isCovered(true)
                                .placesCount(30)
                                .build()
                ))
                .lodgers(new ArrayList<>())
                .build();

        HouseUpdateResponse response = HouseAdapter.updateHouse(String.valueOf(createdHouseId), request);
        softAssert.assertNotNull(response, "Response is null");
        softAssert.assertEquals(response.getId(), createdHouseId, "ID mismatch");
        softAssert.assertEquals(response.getFloorCount(), updatedFloorCount, "Floor count mismatch");
        softAssert.assertEquals(response.getPrice(), updatedPrice, "Price mismatch");
        softAssert.assertNotNull(response.getParkingPlaces(), "Parking places should not be null");
        softAssert.assertTrue(response.getLodgers().isEmpty(), "Lodgers should be empty");
        context.setAttribute(UPDATED_FLOOR_COUNT_KEY, updatedFloorCount);
        context.setAttribute(UPDATED_PRICE_KEY, updatedPrice);
        log.info("House updated successfully with ID: {}", response.getId());
        softAssert.assertAll();
    }

    @Test(groups = {"step6"}, dependsOnGroups = {"step5"})
    public void getHouseTestUpdate(ITestContext context) {
        SoftAssert softAssert = new SoftAssert();

        int createdHouseId = (int) context.getAttribute(HOUSE_ID_KEY);
        int updatedFloorCount = (int) context.getAttribute(UPDATED_FLOOR_COUNT_KEY);
        int updatedPrice = (int) context.getAttribute(UPDATED_PRICE_KEY);

        log.info("Starting update get house test for ID: {}", createdHouseId);
        HouseGetResponse response = HouseAdapter.getHouse(String.valueOf(createdHouseId));
        softAssert.assertNotNull(response, "Response is null");
        softAssert.assertTrue(response.getId() > 0, "ID should be positive");
        softAssert.assertEquals(response.getFloorCount(), updatedFloorCount, "Floor count mismatch");
        softAssert.assertEquals(response.getPrice(), (double) updatedPrice, "Price mismatch");
        softAssert.assertNotNull(response.getParkingPlaces(), "Parking places should not be null");
        log.info("Update house retrieved successfully with ID: {}", response.getId());
        softAssert.assertAll();
    }

    @Test(groups = {"step7"}, dependsOnGroups = {"step6"})
    public void deleteHouseTest(ITestContext context) {
        int createdHouseId = (int) context.getAttribute(HOUSE_ID_KEY);
        log.info("Starting delete house test for ID: {}", createdHouseId);
        HouseAdapter.deleteHouse(String.valueOf(createdHouseId));
        log.info("House deleted successfully with ID: {}", createdHouseId);
    }

    @Test(groups = {"step9"}, dependsOnGroups = {"step8"})
    public void verifyHouseDeletedTest(ITestContext context) {
        int createdHouseId = (int) context.getAttribute(HOUSE_ID_KEY);
        log.info("Verifying house deletion for ID: {}", createdHouseId);
        try {
            HouseAdapter.getHouse(String.valueOf(createdHouseId));
            fail("House should be deleted but was found");
        } catch (Exception e) {
            log.info("House {} does not exist as expected", createdHouseId);
        }
    }
}