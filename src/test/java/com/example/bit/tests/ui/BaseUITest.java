package com.example.bit.tests.ui;

import com.example.bit.config.TestApplicationConfig;
import com.example.bit.pages.ApprovalMoneyPage;
import com.example.bit.utils.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;

/**
 * Base class for all Android UI tests
 * Handles Appium driver initialization and cleanup
 */
@ContextConfiguration(classes = TestApplicationConfig.class)
public class BaseUITest extends AbstractTestNGSpringContextTests {

    private static final Logger logger = LogManager.getLogger(BaseUITest.class);

    protected ApprovalMoneyPage approvalMoneyPage;
    protected AndroidDriver driver;

    // ============================================
    // Android Application Configuration
    // ============================================
    @Value("${app.package:com.example.approvalapp}")
    protected String appPackage;

    @Value("${app.activity:.MainActivity}")
    protected String appActivity;

    // ============================================
    // Android Device Configuration
    // ============================================
    @Value("${app.device.name:emulator-5554}")
    protected String deviceName;

    @Value("${app.device.version:13}")
    protected String platformVersion;

    // ============================================
    // Application Type
    // ============================================
    @Value("${app.type:native}")  // "native" or "mobile_web"
    protected String appType;

    /**
     * Common setup for all Android UI tests: Initialize Android Appium Driver
     * This method runs before each test method
     */
    @BeforeMethod
    public void setUp() throws MalformedURLException {
        logger.info("🚀 Setting up Android Appium driver");

        if ("native".equalsIgnoreCase(appType)) {
            driver = DriverFactory.initializeAndroidDriver(appPackage, appActivity, deviceName, platformVersion);
            logger.info("✅ Android native app driver initialized");
        } else {
            driver = DriverFactory.initializeAndroidWebDriver(deviceName, platformVersion);
            logger.info("✅ Android mobile web driver (Chrome) initialized");
        }
    }

    /**
     * Common cleanup for all Android UI tests: Close Appium Driver
     * This runs after every test and ensures driver is properly closed
     * alwaysRun=true ensures it runs even if test fails
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("✅ Android Appium driver closed successfully");
            } catch (Exception e) {
                logger.warn("⚠️ Error closing Android driver: {}", e.getMessage());
            } finally {
                driver = null;
            }
        }
    }
}