package com.example.bit.tests;

import com.example.bit.model.PaymentRequest;
import com.example.bit.model.PaymentResponse;
import com.example.bit.utils.HttpStatusCode;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BitApplicationTests extends BasePaymentTest {

    @DataProvider(name = "paymentTestData")
    public Object[][] paymentTestData() {
        return new Object[][] {
            { "John Doe", "0524999999", "Jane Smith", "0529111111", 75.23 },
            { "Alice Johnson", "0505555555", "Bob Wilson", "0506666666", 150.50 },
            { "Charlie Brown", "0507777777", "Diana Prince", "0508888888", 10.00 }
        };
    }

    @Test(dataProvider = "paymentTestData")
    public void testPaymentRequestSuccess(String senderName, String senderPhone,
                                          String receiverName, String receiverPhone, Double amount) {
        PaymentRequest payment = PaymentRequest.builder()
                .senderName(senderName)
                .senderPhone(senderPhone)
                .receiverName(receiverName)
                .receiverPhone(receiverPhone)
                .amount(amount)
                .build();

        PaymentResponse response = paymentSender.sendPayment(payment);

        paymentValidator.validate(response, payment);
        paymentValidator.validateTransactionId(response);
        paymentValidator.validateSqsData(response);
    }

    @Test
    public void testPaymentRequestError() {
        PaymentRequest payment = PaymentRequest.builder()
                .senderName("John Doe")
                .senderPhone("0524999999")
                .receiverName("Jane Smith")
                .receiverPhone("0529111111")
                .amount(-75.23)
                .build();

        paymentSender.sendPayment(payment , HttpStatusCode.INTERNAL_SERVER_ERROR ,"invalid amount");
    }


}
