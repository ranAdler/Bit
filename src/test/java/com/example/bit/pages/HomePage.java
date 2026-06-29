package com.example.bit.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for Android Home Screen
 */
public class HomePage extends BasePage {

    private static final Logger logger = LogManager.getLogger(HomePage.class);

    // ============================================
    // Android Page Elements
    // ============================================
    @AndroidFindBy(id = "com.example.approvalapp:id/home_page")
    private WebElement homePagePath;

    @AndroidFindBy(id = "com.example.approvalapp:id/info_icon")
    private WebElement infoIconPath;

    @AndroidFindBy(id = "com.example.approvalapp:id/data")
    private WebElement dataPath;

    @AndroidFindBy(id = "com.example.approvalapp:id/move_to_approval")
    private WebElement moveToApprovalPath;

    public HomePage(AndroidDriver driver) {
        super(driver);
        try {
            wait.until(ExpectedConditions.visibilityOf(homePagePath));
            logger.info("✅ HomePage loaded");
        } catch (Exception e) {
            logger.warn("⚠️ Timeout waiting for home page: {}", e.getMessage());
        }
    }

    public HomePage selectInfoIcon() {
        logger.info("Clicking info icon");
        infoIconPath.click();
        wait.until(ExpectedConditions.visibilityOf(dataPath));
        logger.info("✅ Data section displayed");
        return this;
    }

    public ApprovalMoneyPage moveToApproval() {
        logger.info("Moving to approval page");
        moveToApprovalPath.click();
        return new ApprovalMoneyPage(driver);
    }
}
