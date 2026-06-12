package utils;

import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.concurrent.TimeUnit;

@Log4j2
public class TestListener implements ITestListener {

    // Автоматическое отключение цветов в Jenkins
    private static final boolean IS_JENKINS = System.getenv("JENKINS_HOME") != null
            || System.getenv("JENKINS_URL") != null
            || System.getProperty("jenkins") != null;

    private static final boolean ENABLE_COLORS = !IS_JENKINS;

    private static final String RESET = ENABLE_COLORS ? "\u001B[0m" : "";
    private static final String GREEN = ENABLE_COLORS ? "\u001B[32m" : "";
    private static final String RED = ENABLE_COLORS ? "\u001B[31m" : "";
    private static final String YELLOW = ENABLE_COLORS ? "\u001B[33m" : "";
    private static final String CYAN = ENABLE_COLORS ? "\u001B[36m" : "";
    private static final String PURPLE = ENABLE_COLORS ? "\u001B[35m" : "";
    private static final String BOLD = ENABLE_COLORS ? "\u001B[1m" : "";

    static {
        if (IS_JENKINS) {
            log.info("Running in Jenkins environment - colors disabled");
        }
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        log.info(CYAN + "======================================== STARTING TEST {} ========================================" + RESET,
                iTestResult.getName());
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        log.info(GREEN + "======================================== FINISHED TEST {} Duration: {} sec ========================================" + RESET,
                iTestResult.getName(), getExecutionTime(iTestResult));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        log.error(RED + "======================================== FAILED TEST {} Duration: {} sec ========================================" + RESET,
                iTestResult.getName(), getExecutionTime(iTestResult));
        utils.AllureUtils.takeScreenshot();
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        log.warn(YELLOW + "======================================== SKIPPING TEST {} ========================================" + RESET,
                iTestResult.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        // Не используется
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        log.info(PURPLE + BOLD + "========== STARTING SUITE: {} ==========" + RESET, iTestContext.getName());
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        log.info(PURPLE + BOLD + "========== FINISHED SUITE: {} ==========" + RESET, iTestContext.getName());
    }

    private long getExecutionTime(ITestResult iTestResult) {
        return TimeUnit.MILLISECONDS.toSeconds(iTestResult.getEndMillis() - iTestResult.getStartMillis());
    }
}