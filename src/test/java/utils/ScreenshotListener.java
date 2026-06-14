package utils;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class ScreenshotListener implements IInvokedMethodListener {

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod() && testResult.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(testResult.getName());
        }
    }

    @Attachment(value = "Screenshot on failure - {testName}", type = "image/png")
    private byte[] takeScreenshot(String testName) {
        return Selenide.screenshot(org.openqa.selenium.OutputType.BYTES);
    }
}