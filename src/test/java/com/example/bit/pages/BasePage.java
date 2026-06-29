package com.example.bit.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Base page class for Android Appium page objects
 * Handles common page operations and element initialization
 */
public class BasePage {
    protected AndroidDriver driver;
    protected JavascriptExecutor jsExecutor;
    protected WebDriverWait wait;
    private static final int TIMEOUT_SECONDS = 10;

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
        // Use AppiumFieldDecorator for Android element initialization
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    /**
     * Execute JavaScript (Android Appium compatible)
     */
    public Object executeScript(String script, Object... args) {
        return jsExecutor.executeScript(script, args);
    }

    /**
     * Scroll to element
     */
    public void scrollToElement(WebElement element) {
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Refresh page
     */
    public void refreshPage() {
        driver.navigate().refresh();
    }
}