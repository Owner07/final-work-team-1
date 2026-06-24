package ui;

import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;
import wrappers.GetStatus;
import wrappers.Input;
import wrappers.Select;
import wrappers.TableValue;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static org.testng.Assert.*;


@Log4j2
public class HouseTest extends BaseTest{


    @Test
    public void checkOneHouseStatus() {
        Select.selectByText("Houses", "Read one by ID");
        log.info("Open page Read one by ID houses");
        Input.inputRead("1");
        assertEquals(GetStatus.getStatus(), "Status: 200 ok");
    }

    @Test
    public void checkOneHouses() {
        Select.selectByText("Houses", "Read one by ID");
        log.info("Opened page Read one by ID houses");
        Input.inputRead("2");
        List<String> allIds = TableValue.getAllValues(TableValue.HOUSES_TABLE, "ID");
        log.info("All IDs found this: {}", allIds);
        assertFalse(allIds.isEmpty(), "Table is empty or column not found");
        assertTrue(allIds.contains("2"), "ID '2' not found. Available IDs: " + allIds);
    }
}
