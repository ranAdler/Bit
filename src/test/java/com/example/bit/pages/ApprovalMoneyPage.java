package com.example.bit.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for Android Approval Money Screen
 * Represents the approval money confirmation page in the Android app
 */
public class ApprovalMoneyPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(ApprovalMoneyPage.class);

    // ============================================
    // Android Page Elements
    // ============================================
    // Format: package:id/element_name
    // Get these from Appium Inspector or adb uiautomator dump

    @AndroidFindBy(id = "com.example.approvalapp:id/approve_button")
    private WebElement approveTransferButton;

    @AndroidFindBy(id = "com.example.approvalapp:id/confirmation_message")
    private WebElement confirmationMessage;

    @AndroidFindBy(id = "com.example.approvalapp:id/confirmation_title")
    private WebElement confirmationTitle;

    @AndroidFindBy(id = "com.example.approvalapp:id/close_button")
    private WebElement closeButton;

    public ApprovalMoneyPage(AndroidDriver driver) {
        super(driver);
        try {
            wait.until(ExpectedConditions.visibilityOf(confirmationMessage));
            logger.info("✅ ApprovalMoneyPage loaded");
        } catch (Exception e) {
            logger.warn("⚠️ Timeout waiting for confirmation message: {}", e.getMessage());
        }
    }

    /**
     * Verify that the approval popup is displayed
     */
    public boolean isApprovalPopupDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(confirmationTitle));
            logger.info("✅ Approval popup is displayed");
            return confirmationTitle.isDisplayed();
        } catch (Exception e) {
            logger.warn("⚠️ Approval popup not found: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Click on the "Approve Transfer" button
     */
    public ApprovalMoneyPage clickApproveTransferButton() {
        logger.info("Clicking approve button");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(approveTransferButton));
        button.click();
        return this;
    }

    /**
     * Get the confirmation message text
     * Expected: "The money was transferred to your account" or Hebrew equivalent
     */
    public String getConfirmationMessage() {
        logger.info("Getting confirmation message text");
        wait.until(ExpectedConditions.visibilityOf(confirmationMessage));
        String messageText = confirmationMessage.getText();
        logger.info("Confirmation message: {}", messageText);
        return messageText;
    }

    /**
     * Verify the confirmation message is displayed and contains expected text
     */
    public boolean isConfirmationMessageDisplayed(String expectedText) {
        logger.info("Checking if confirmation message contains: {}", expectedText);
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(confirmationMessage, expectedText));
            boolean isDisplayed = confirmationMessage.getText().contains(expectedText);
            logger.info("Confirmation message displayed: {}", isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.warn("⚠️ Confirmation message not found or timeout: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get the confirmation title text
     */
    public String getConfirmationTitle() {
        logger.info("Getting confirmation title text");
        wait.until(ExpectedConditions.visibilityOf(confirmationTitle));
        String titleText = confirmationTitle.getText();
        logger.info("Confirmation title: {}", titleText);
        return titleText;
    }

    /**
     * Close the popup
     */
    public HomePage closePopup() {
        logger.info("Closing confirmation popup");
        WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(closeButton));
        closeBtn.click();
        return new HomePage(driver);
    }

    /**
     * Wait for popup to disappear
     */
    public void waitForPopupToDisappear() {
        logger.info("Waiting for popup to disappear");
        wait.until(ExpectedConditions.invisibilityOf(confirmationTitle));
        logger.info("✅ Popup disappeared");
    }

    /**
     * Check if approve button is enabled and clickable
     */
    public boolean isApproveButtonEnabled() {
        logger.info("Checking if approve button is enabled");
        boolean isEnabled = approveTransferButton.isEnabled();
        logger.info("Approve button enabled: {}", isEnabled);
        return isEnabled;
    }

    /**
     * Check if all page elements are displayed
     */
    public boolean isPageLoaded() {
        logger.info("Checking if approval money page is fully loaded");
        try {
            return confirmationTitle.isDisplayed() &&
                   confirmationMessage.isDisplayed() &&
                   approveTransferButton.isDisplayed();
        } catch (Exception e) {
            logger.error("Page not fully loaded: {}", e.getMessage());
            return false;
        }
    }
}