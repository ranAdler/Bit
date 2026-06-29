package com.example.bit.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestNG Listener for UI Tests
 * Handles test execution logging, screenshot capture on failure, and browser cleanup
 */
public class UITestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(UITestListener.class);
    private static final String SCREENSHOT_DIR = "screenshots";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    /**
     * Called before test execution starts
     */
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testClass = result.getTestClass().getRealClass().getSimpleName();

        // Log test start
        logger.info("═".repeat(80));
        logger.info("🧪 TEST STARTED: {}.{}", testClass, testName);
        logger.info("─".repeat(80));
        logger.info("Test Description: {}", result.getMethod().getDescription());
        logger.info("Test Parameters: {}", formatParameters(result.getParameters()));
        logger.info("─".repeat(80));
    }

    /**
     * Called when test passes
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testClass = result.getTestClass().getRealClass().getSimpleName();
        long duration = result.getEndMillis() - result.getStartMillis();

        logger.info("─".repeat(80));
        logger.info("✅ TEST PASSED: {}.{}", testClass, testName);
        logger.info("   Duration: {} ms", duration);
        logger.info("═".repeat(80));
    }

    /**
     * Called when test fails
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testClass = result.getTestClass().getRealClass().getSimpleName();
        long duration = result.getEndMillis() - result.getStartMillis();
        Throwable exception = result.getThrowable();

        logger.error("─".repeat(80));
        logger.error("❌ TEST FAILED: {}.{}", testClass, testName);
        logger.error("   Duration: {} ms", duration);
        logger.error("   Error Message: {}", exception != null ? exception.getMessage() : "Unknown");

        // Capture screenshot and close browser
        WebDriver driver = null;
        try {
            Object testObject = result.getInstance();
            if (testObject != null) {
                driver = getWebDriverFromTestObject(testObject);
                if (driver != null) {
                    try {
                        captureScreenshot(driver, testClass, testName);
                        logger.info("   📸 Screenshot captured");
                    } catch (Exception screenshotEx) {
                        logger.warn("   ⚠️  Failed to capture screenshot: {}", screenshotEx.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("   ⚠️  Error accessing test object: {}", e.getMessage());
        }

        // Always try to close the browser, even if screenshot failed
        if (driver != null) {
            try {
                driver.quit();
                logger.info("   🔒 Browser closed");
            } catch (Exception e) {
                logger.warn("   ⚠️  Failed to close browser: {}", e.getMessage());
                // Try force close via process if normal quit fails
                try {
                    Runtime.getRuntime().exec("pkill -f chromedriver");
                    logger.info("   🔒 Browser force closed");
                } catch (Exception ex) {
                    logger.warn("   ⚠️  Could not force close browser: {}", ex.getMessage());
                }
            }
        }

        // Log exception details
        if (exception != null) {
            logger.error("   Stack Trace:", exception);
        }

        logger.error("═".repeat(80));
    }

    /**
     * Called when test is skipped
     * Also captures screenshot if skipped due to setUp failure
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testClass = result.getTestClass().getRealClass().getSimpleName();

        logger.warn("─".repeat(80));
        logger.warn("⊘ TEST SKIPPED: {}.{}", testClass, testName);
        logger.warn("   Reason: {}", result.getSkipCausedBy());

        // If skipped due to setUp failure, capture screenshot
        Throwable skipThrowable = result.getThrowable();
        if (skipThrowable != null && result.getSkipCausedBy().toString().contains("setUp")) {
            logger.warn("   ⚠️  Test skipped due to setUp failure - capturing screenshot");

            WebDriver driver = null;
            try {
                Object testObject = result.getInstance();
                if (testObject != null) {
                    driver = getWebDriverFromTestObject(testObject);
                    if (driver != null) {
                        try {
                            captureScreenshot(driver, testClass, testName);
                            logger.info("   📸 Screenshot captured");
                        } catch (Exception screenshotEx) {
                            logger.warn("   ⚠️  Failed to capture screenshot: {}", screenshotEx.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("   ⚠️  Error accessing test object: {}", e.getMessage());
            }

            // Always try to close the browser
            if (driver != null) {
                try {
                    driver.quit();
                    logger.info("   🔒 Browser closed");
                } catch (Exception e) {
                    logger.warn("   ⚠️  Failed to close browser: {}", e.getMessage());
                }
            }
        }

        logger.warn("═".repeat(80));
    }

    /**
     * Called when test fails but within success percentage
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testClass = result.getTestClass().getRealClass().getSimpleName();

        logger.warn("─".repeat(80));
        logger.warn("⚠️  TEST FAILED BUT WITHIN SUCCESS %: {}.{}", testClass, testName);
        logger.warn("═".repeat(80));
    }

    /**
     * Called at the beginning of test suite execution
     */
    @Override
    public void onStart(ITestContext context) {
        logger.info("╔" + "═".repeat(78) + "╗");
        logger.info("║ 🚀 TEST SUITE STARTED: {}", padRight(context.getName(), 58));
        logger.info("║ Total Tests: {}", padRight(String.valueOf(context.getAllTestMethods().length), 66));
        logger.info("║ Start Time: {}", padRight(new Date().toString(), 64));
        logger.info("╚" + "═".repeat(78) + "╝");

        // Create screenshot directory
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
            logger.info("   📁 Screenshot directory created: {}", SCREENSHOT_DIR);
        }
    }

    /**
     * Called at the end of test suite execution
     */
    @Override
    public void onFinish(ITestContext context) {
        int totalTests = context.getAllTestMethods().length;
        int passedTests = context.getPassedTests().size();
        int failedTests = context.getFailedTests().size();
        int skippedTests = context.getSkippedTests().size();
        long duration = context.getEndDate().getTime() - context.getStartDate().getTime();

        double passPercentage = totalTests > 0 ? (double) passedTests / totalTests * 100 : 0;

        logger.info("╔" + "═".repeat(78) + "╗");
        logger.info("║ 🏁 TEST SUITE FINISHED: {}", padRight(context.getName(), 55));
        logger.info("╠" + "═".repeat(78) + "╣");
        logger.info("║ Total Tests: {} | ✅ Passed: {} | ❌ Failed: {} | ⊘ Skipped: {}",
                padRight(String.valueOf(totalTests), 8),
                padRight(String.valueOf(passedTests), 8),
                padRight(String.valueOf(failedTests), 8),
                padRight(String.valueOf(skippedTests), 8));
        logger.info("║ Success Rate: {}", padRight(String.format("%.2f%%", passPercentage), 67));
        logger.info("║ Duration: {}", padRight(formatDuration(duration), 71));
        logger.info("║ End Time: {}", padRight(new Date().toString(), 72));
        logger.info("╚" + "═".repeat(78) + "╝");
    }

    // ==================== Helper Methods ====================

    /**
     * Get WebDriver from test object (searches up inheritance chain)
     */
    private WebDriver getWebDriverFromTestObject(Object testObject) {
        try {
            Class<?> currentClass = testObject.getClass();

            // Search up the inheritance chain for the driver field
            while (currentClass != null) {
                try {
                    java.lang.reflect.Field driverField = currentClass.getDeclaredField("driver");
                    driverField.setAccessible(true);
                    Object driver = driverField.get(testObject);
                    if (driver instanceof WebDriver) {
                        return (WebDriver) driver;
                    }
                } catch (NoSuchFieldException e) {
                    // Field not found in this class, try parent
                }
                currentClass = currentClass.getSuperclass();
            }

            logger.debug("Could not retrieve WebDriver from test object - driver field not found in class hierarchy");
            return null;
        } catch (Exception e) {
            logger.debug("Error retrieving WebDriver from test object: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot on test failure
     */
    private void captureScreenshot(WebDriver driver, String testClass, String testName) {
        if (driver == null) {
            logger.warn("   Driver is null, cannot capture screenshot");
            return;
        }

        try {
            // Ensure screenshot directory exists
            File screenshotDirectory = new File(SCREENSHOT_DIR);
            if (!screenshotDirectory.exists()) {
                screenshotDirectory.mkdirs();
            }

            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);

            if (srcFile == null || !srcFile.exists()) {
                logger.warn("   Screenshot file is null or does not exist");
                return;
            }

            // Create screenshot filename with timestamp
            String timestamp = dateFormat.format(new Date());
            String screenshotName = String.format("%s/%s_%s_%s.png",
                    SCREENSHOT_DIR, testClass, testName, timestamp);

            // Ensure parent directory exists
            File screenshotFile = new File(screenshotName);
            screenshotFile.getParentFile().mkdirs();

            // Save screenshot with better error handling
            try (FileOutputStream fos = new FileOutputStream(screenshotName)) {
                byte[] imageBytes = java.nio.file.Files.readAllBytes(srcFile.toPath());
                fos.write(imageBytes);
                fos.flush();
                logger.info("   📸 Screenshot saved to: {}", screenshotName);
            }
        } catch (IOException e) {
            logger.error("   ❌ IO Error capturing screenshot: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("   ❌ Error capturing screenshot: {}", e.getMessage(), e);
        }
    }

    /**
     * Format test parameters for logging
     */
    private String formatParameters(Object[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return "None";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(parameters[i]);
        }
        return sb.toString();
    }

    /**
     * Pad string to right
     */
    private String padRight(String str, int length) {
        if (str.length() >= length) return str;
        return str + " ".repeat(length - str.length());
    }

    /**
     * Format duration to readable format
     */
    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        if (hours > 0) {
            return String.format("%d h %d m %d s", hours, minutes % 60, seconds % 60);
        } else if (minutes > 0) {
            return String.format("%d m %d s", minutes, seconds % 60);
        } else {
            return String.format("%d s", seconds);
        }
    }
}