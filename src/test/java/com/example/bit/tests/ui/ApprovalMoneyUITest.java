package com.example.bit.tests.ui;

import com.example.bit.pages.ApprovalMoneyPage;
import com.example.bit.pages.HomePage;
import com.example.bit.utils.UITestMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ApprovalMoneyUITest extends BaseUITest {

    private static final Logger logger = LogManager.getLogger(ApprovalMoneyUITest.class);

    private HomePage homePage;
    /**
     * Initialize Page Objects before each test
     * Starts from HomePage
     */
    @BeforeMethod
    public void initializePageObjects() {
        logger.info("initializePageObjects");
        homePage = new HomePage(driver);
    }



    @Test(priority = 10)
    public void testVerifyConfirmationMessage() {
        approvalMoneyPage = homePage.moveToApproval();

        String expectedMessage = UITestMessages.MONEY_TRANSFER_CONFIRMATION;
        boolean isConfirmed = approvalMoneyPage
                .clickApproveTransferButton()
                .isConfirmationMessageDisplayed(expectedMessage);
        assertTrue(isConfirmed, "Error isConfirmationMessageDisplayed is not displayed");

        String actualMessage = approvalMoneyPage.getConfirmationMessage();
        assertEquals(actualMessage, UITestMessages.MONEY_TRANSFER_CONFIRMATION,
                "Confirmation Message is not displayed");
    }


}