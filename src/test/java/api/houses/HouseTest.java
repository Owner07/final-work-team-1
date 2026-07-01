package tests.houses;

import api.adapters.houses.HouseAdapter;
import api.models.houses.create.HouseCreateRequest;
import api.models.houses.create.HouseCreateResponse;
import api.models.houses.create.HouseCreateRequest.ParkingPlace;
import api.models.houses.get.HouseGetResponse;
import api.models.houses.update.HouseUpdateRequest;
import api.models.houses.update.HouseUpdateResponse;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

@Log4j2
public class HouseTest {

    private static int createdHouseId;

    // Добавьте в тест для проверки
    @Test
    public void checkSchemaExists() {
        URL resource = getClass().getClassLoader().getResource("schemas/houses/HouseCreateResponse.json");
        System.out.println("Schema path: " + resource);
        assertNotNull(resource, "Schema file not found!");
    }

    @Test(priority = 1)
    public void createHouseTest() {
        SoftAssert softAssert = new SoftAssert();

        try {
            log.info("Starting create house test");

            HouseCreateRequest request = HouseCreateRequest.builder()
                    .floorCount(5)
                    .id(178)
                    .parkingPlaces(Arrays.asList(
                            ParkingPlace.builder()
                                    .id(10)
                                    .isCovered(true)
                                    .isWarm(true)
                                    .placesCount(24)
                                    .build()
                    ))
                    .price(0)
                    .build();

            log.info("Create request: {}", request);
            HouseCreateResponse response = HouseAdapter.createHouse(request);
            log.info("Create response: {}", response);

            softAssert.assertNotNull(response, "Response is null");
            softAssert.assertTrue(response.getId() > 0, "ID should be positive");
            softAssert.assertEquals(response.getFloorCount(), 5, "Floor count mismatch");
            softAssert.assertEquals(response.getPrice(), 0, "Price mismatch");
            softAssert.assertNotNull(response.getParkingPlaces(), "Parking places should not be null");
            softAssert.assertEquals(response.getParkingPlaces().size(), 1, "Parking places size mismatch");
            softAssert.assertEquals(response.getParkingPlaces().get(0).getPlacesCount(), 24, "Places count mismatch");
            softAssert.assertTrue(response.getParkingPlaces().get(0).isWarm(), "Parking should be warm");
            softAssert.assertTrue(response.getParkingPlaces().get(0).isCovered(), "Parking should be covered");

            createdHouseId = response.getId();
            log.info("House created successfully with ID: {}", createdHouseId);

        } catch (Exception e) {
            log.error("Create house test failed", e);
            softAssert.fail("Test failed with exception: " + e.getMessage());
        } finally {
            softAssert.assertAll();
        }
    }

    @Test(priority = 2, dependsOnMethods = "createHouseTest")
    public void getHouseTest() {
        SoftAssert softAssert = new SoftAssert();

        try {
            log.info("Starting get house test for ID: {}", createdHouseId);

            HouseGetResponse response = HouseAdapter.getHouse(String.valueOf(createdHouseId));
            log.info("Get response: {}", response);

            softAssert.assertNotNull(response, "Response is null");
            softAssert.assertEquals(response.getId(), createdHouseId, "ID mismatch");
            softAssert.assertEquals(response.getFloorCount(), 5, "Floor count mismatch");
            softAssert.assertEquals(response.getPrice(), 0.0, "Price mismatch");
            softAssert.assertNotNull(response.getParkingPlaces(), "Parking places should not be null");
            softAssert.assertEquals(response.getParkingPlaces().size(), 1, "Parking places size mismatch");
            softAssert.assertEquals(response.getParkingPlaces().get(0).getPlacesCount(), 24, "Places count mismatch");
            softAssert.assertTrue(response.getParkingPlaces().get(0).isWarm(), "Parking should be warm");
            softAssert.assertTrue(response.getParkingPlaces().get(0).isCovered(), "Parking should be covered");
            softAssert.assertNotNull(response.getLodgers(), "Lodgers should not be null");

            log.info("House retrieved successfully with ID: {}", response.getId());

        } catch (Exception e) {
            log.error("Get house test failed for ID: {}", createdHouseId, e);
            softAssert.fail("Test failed with exception: " + e.getMessage());
        } finally {
            softAssert.assertAll();
        }
    }

    @Test(priority = 3, dependsOnMethods = "createHouseTest")
    public void updateHouseTest() {
        SoftAssert softAssert = new SoftAssert();

        try {
            log.info("Starting update house test for ID: {}", createdHouseId);

            HouseUpdateRequest request = HouseUpdateRequest.builder()
                    .id(createdHouseId)
                    .floorCount(10)
                    .price(1100)
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

            log.info("Update request: {}", request);
            HouseUpdateResponse response = HouseAdapter.updateHouse(String.valueOf(createdHouseId), request);
            log.info("Update response: {}", response);

            softAssert.assertNotNull(response, "Response is null");
            softAssert.assertEquals(response.getId(), createdHouseId, "ID mismatch");
            softAssert.assertEquals(response.getFloorCount(), 10, "Floor count mismatch");
            softAssert.assertEquals(response.getPrice(), 1100, "Price mismatch");
            softAssert.assertNotNull(response.getParkingPlaces(), "Parking places should not be null");
            softAssert.assertEquals(response.getParkingPlaces().size(), 1, "Parking places size mismatch");
            softAssert.assertEquals(response.getParkingPlaces().get(0).getPlacesCount(), 30, "Places count mismatch");
            softAssert.assertNotNull(response.getLodgers(), "Lodgers should not be null");
            softAssert.assertTrue(response.getLodgers().isEmpty(), "Lodgers should be empty");

            log.info("House updated successfully with ID: {}", response.getId());

        } catch (Exception e) {
            log.error("Update house test failed for ID: {}", createdHouseId, e);
            softAssert.fail("Test failed with exception: " + e.getMessage());
        } finally {
            softAssert.assertAll();
        }
    }

    @Test(priority = 4, dependsOnMethods = "updateHouseTest")
    public void deleteHouseTest() {
        try {
            log.info("Starting delete house test for ID: {}", createdHouseId);

            HouseAdapter.deleteHouse(String.valueOf(createdHouseId));

            log.info("House deleted successfully with ID: {}", createdHouseId);

        } catch (Exception e) {
            log.error("Delete house test failed for ID: {}", createdHouseId, e);
            fail("Delete failed with exception: " + e.getMessage());
        }
    }

    @Test(priority = 5, dependsOnMethods = "deleteHouseTest")
    public void verifyHouseDeletedTest() {
        try {
            log.info("Verifying house deletion for ID: {}", createdHouseId);

            HouseAdapter.getHouse(String.valueOf(createdHouseId));
            fail("House should be deleted but was found");

        } catch (Exception e) {
            log.info("House {} does not exist as expected", createdHouseId);
            // Ожидаемая ошибка
            assert e.getMessage().contains("404") : "Expected 404 error but got: " + e.getMessage();
        }
    }
}