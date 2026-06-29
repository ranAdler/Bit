package com.example.bit.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ApprovalMoneyPage extends BasePage {

    @FindBy(id = "approvalButton")
    private WebElement approveTransferButton;

    @FindBy(id = "confirmationMessage")
    private WebElement confirmationMessage;

    @FindBy(className = "popup-overlay")
    private WebElement popupOverlay;

    // Alternative CSS selectors (examples - adjust as needed for actual HTML)
    private By APPROVE_BUTTON_CSS = By.cssSelector("button.approve-btn");
    private By CONFIRMATION_TEXT_CSS = By.cssSelector(".confirmation-message");
    private By CLOSE_BUTTON = By.id("closeButton");

    public ApprovalMoneyPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOf(confirmationMessage));
    }

    /**
     * Verify that the approval popup is displayed
     */
    public boolean isApprovalPopupDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(popupOverlay));
            return popupOverlay.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click on the "אשר העברה" (Approve Transfer) button
     */
    public ApprovalMoneyPage clickApproveTransferButton() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(approveTransferButton));
        button.click();
        return this;
    }



    /**
     * Get the confirmation message text
     * Expected: "הכסף הועבר לחשבונך" (The money was transferred to your account)
     */
    public String getConfirmationMessage() {
        wait.until(ExpectedConditions.visibilityOf(confirmationMessage));
        return confirmationMessage.getText();
    }

    /**
     * Verify the confirmation message is displayed and contains expected text
     */
    public boolean isConfirmationMessageDisplayed(String expectedText) {
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(confirmationMessage, expectedText));
            return confirmationMessage.getText().contains(expectedText);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Close the popup
     */
    public HomePage closePopup() {
        WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(CLOSE_BUTTON));
        closeBtn.click();
        return new HomePage(driver);
    }

    /**
     * Wait for popup to disappear
     */
    public void waitForPopupToDisappear() {
        wait.until(ExpectedConditions.invisibilityOf(popupOverlay));
    }
}