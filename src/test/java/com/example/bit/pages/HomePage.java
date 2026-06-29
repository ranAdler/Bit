package com.example.bit.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    @FindBy(className = "homePage")
    private WebElement homePagePath;

    @FindBy(className = "infoIcon")
    private WebElement infoIconPath;

    @FindBy(id = "data")
    private WebElement dataPath;

    @FindBy(className = "move-to-approval")
    private WebElement moveToApprovalPath;

    public HomePage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOf(homePagePath));
    }

    public HomePage selectInfoIcon() {
        infoIconPath.click();
        wait.until(ExpectedConditions.visibilityOf(dataPath));
        return this;
    }

    public ApprovalMoneyPage moveToApproval() {
        moveToApprovalPath.click();
        return new ApprovalMoneyPage(driver);
    }
}
