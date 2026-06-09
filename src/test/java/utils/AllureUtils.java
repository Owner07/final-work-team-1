package utils;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;

public class AllureUtils {

    @Attachment(value = "screenshot", type = "image/png")
    public static byte[] takeScreenshot() {
        return Selenide.screenshot(OutputType.BYTES);
    }

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] takeScreenshot(String name) {
        return Selenide.screenshot(OutputType.BYTES);
    }
}