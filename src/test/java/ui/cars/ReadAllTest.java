package ui.cars;

import org.testng.annotations.Test;
import ui.BaseTest;
import utils.PropertyReader;

import static org.testng.Assert.assertEquals;

public class ReadAllTest extends BaseTest {

    private static final String user = PropertyReader.getProperty("user");
    private static final String password = PropertyReader.getProperty("password");

    @Test
    public void openReadAll() {
        loginPage.open()
                .login(user, password);
        carsPage.openReadAll();
    }

    @Test
    public void sortID() {
        loginPage.open()
                .login(user, password);
        carsPage.openReadAll();
        carsPage.sortByID();
        String firstId = carsPage.getFirstCarId();
        assertEquals(firstId, "3", "Первый ID должен быть 3");
    }
}