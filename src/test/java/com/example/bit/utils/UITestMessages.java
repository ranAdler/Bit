package com.example.bit.utils;

/**
 * Constants for UI test messages and expected text values
 * Centralized location for all test messages and expected UI text
 */
public class UITestMessages {

    // ======================= Approval Money Messages =======================

    // Hebrew messages
    public static final String CONFIRM_TRANSFER_BUTTON = "אשר העברה";
    public static final String MONEY_TRANSFER_CONFIRMATION = "הכסף הועבר לחשבונך";

    // English messages (for documentation)
    public static final String CONFIRM_TRANSFER_BUTTON_EN = "Approve Transfer";
    public static final String MONEY_TRANSFER_CONFIRMATION_EN = "The money was transferred to your account";

    // ======================= Error Messages =======================

    public static final String POPUP_NOT_DISPLAYED = "Approval popup should be displayed";
    public static final String POPUP_NOT_FOUND = "Step 2 FAILED: Approval popup with button not found";
    public static final String HOME_PAGE_INITIALIZATION_ERROR = "HomePage should be initialized";

}