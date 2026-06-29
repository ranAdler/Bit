package com.example.bit.utils;

import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.time.Duration;

/**
 * Android Appium Driver Factory
 * Handles initialization of Android driver for native app and mobile web testing
 */
public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final String APPIUM_SERVER_URL = "http://localhost:4723";
    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);

    /**
     * Initialize Android Appium Driver for native app testing
     *
     * @param appPackage App package name (e.g., "com.example.approvalapp")
     * @param appActivity App activity name (e.g., ".MainActivity")
     * @param deviceName Device ID from 'adb devices' (e.g., "emulator-5554")
     * @param platformVersion Android version (e.g., "13", "14")
     * @return AndroidDriver instance
     * @throws MalformedURLException if Appium server URL is invalid
     */
    public static AndroidDriver initializeAndroidDriver(
            String appPackage,
            String appActivity,
            String deviceName,
            String platformVersion) throws MalformedURLException {

        logger.info("🚀 Initializing Android Appium Driver");
        logger.info("   Package: {}", appPackage);
        logger.info("   Activity: {}", appActivity);
        logger.info("   Device: {}", deviceName);
        logger.info("   Android Version: {}", platformVersion);

        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Required capabilities
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("app", getAppPath());
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformVersion", platformVersion);

        // Optional capabilities for stability and permissions
        capabilities.setCapability("autoGrantPermissions", true);
        capabilities.setCapability("autoWebview", false);
        capabilities.setCapability("noReset", false);
        capabilities.setCapability("newCommandTimeout", 300);

        try {
            AndroidDriver driver = new AndroidDriver(new java.net.URL(APPIUM_SERVER_URL), capabilities);
            driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
            logger.info("✅ Android driver initialized successfully");
            return driver;
        } catch (MalformedURLException e) {
            logger.error("❌ Failed to initialize Android driver: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Initialize Android Appium Driver for mobile web testing (Chrome browser)
     *
     * @param deviceName Device ID from 'adb devices'
     * @param platformVersion Android version
     * @return AndroidDriver instance
     * @throws MalformedURLException if Appium server URL is invalid
     */
    public static AndroidDriver initializeAndroidWebDriver(
            String deviceName,
            String platformVersion) throws MalformedURLException {

        logger.info("🚀 Initializing Android Mobile Web Driver (Chrome)");
        logger.info("   Device: {}", deviceName);
        logger.info("   Android Version: {}", platformVersion);

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("chromeOptions", "w3c");
        capabilities.setCapability("newCommandTimeout", 300);

        try {
            AndroidDriver driver = new AndroidDriver(new java.net.URL(APPIUM_SERVER_URL), capabilities);
            driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
            logger.info("✅ Android mobile web driver initialized successfully");
            return driver;
        } catch (MalformedURLException e) {
            logger.error("❌ Failed to initialize Android mobile web driver: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Get path to Android APK file
     * Customize based on where your app is built
     *
     * @return Path to APK file
     */
    private static String getAppPath() {
        String appPath = System.getProperty("app.path");
        if (appPath == null) {
            appPath = "src/test/resources/app/app-debug.apk";
        }
        logger.info("   APK Path: {}", appPath);
        return appPath;
    }
}