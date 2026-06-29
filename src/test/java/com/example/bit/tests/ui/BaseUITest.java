package com.example.bit.tests.ui;

import com.example.bit.pages.ApprovalMoneyPage;
import com.example.bit.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

@SpringBootTest
public class BaseUITest extends AbstractTestNGSpringContextTests {

    protected ApprovalMoneyPage approvalMoneyPage;
    protected WebDriver driver;

    @Value("${app.url}")
    protected String appUrl;

    /**
     * Common setup for all UI tests: Initialize WebDriver and navigate to app
     */
    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.initializeDriver("chrome");
        driver.get(appUrl);
    }

    /**
     * Common cleanup for all UI tests: Close WebDriver
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}