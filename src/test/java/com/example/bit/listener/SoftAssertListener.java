package com.example.bit.listener;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.asserts.SoftAssert;

public class SoftAssertListener extends TestListenerAdapter {

    private static final ThreadLocal<SoftAssert> softAssertThreadLocal = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        softAssertThreadLocal.set(new SoftAssert());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        getSoftAssert().assertAll();
        softAssertThreadLocal.remove();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getSoftAssert().assertAll();
        softAssertThreadLocal.remove();
    }

    public static SoftAssert getSoftAssert() {
        SoftAssert softAssert = softAssertThreadLocal.get();
        if (softAssert == null) {
            throw new RuntimeException("SoftAssert is not initialized. Make sure SoftAssertListener is configured in testng.xml");
        }
        return softAssert;
    }
}