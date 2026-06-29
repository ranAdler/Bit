package com.example.bit.tests.ui;

import com.example.bit.config.TestApplicationConfig;
import com.example.bit.pages.ApprovalMoneyPage;
import com.example.bit.utils.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

@ContextConfiguration(classes = TestApplicationConfig.class)
public class BaseUITest extends AbstractTestNGSpringContextTests {

    private static final Logger logger = LogManager.getLogger(BaseUITest.class);

    protected ApprovalMoneyPage approvalMoneyPage;
    protected WebDriver driver;

    @Value("${app.url}")
    protected String appUrl;

    @Value("${app.browser:CHROME}")
    protected String browserName;

    /**
     * Common setup for all UI tests: Initialize WebDriver and navigate to app
     */
    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.initializeDriver(browserName);
        driver.get(appUrl);
    }

    /**
     * Common cleanup for all UI tests: Close WebDriver
     * This runs after every test and ensures browser is properly closed
     * alwaysRun=true ensures it runs even if test fails
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("✅ Browser closed successfully in tearDown");
            } catch (Exception e) {
                logger.warn("⚠️Error closing browser in tearDown: {}", e.getMessage());
                // Force close if normal quit fails
                try {
                    Runtime.getRuntime().exec("pkill -f chromedriver");
                    logger.info("✅ Browser force closed via pkill");
                } catch (Exception ex) {
                    logger.error("❌ Failed to force close browser: {}", ex.getMessage());
                }
            } finally {
                driver = null;
            }
        }
    }
}