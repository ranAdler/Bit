package com.example.bit.tests;

import com.example.bit.model.PaymentRequest;
import com.example.bit.model.PaymentResponse;
import org.testng.annotations.Test;

public class BitApplicationTests extends BasePaymentTest {

    @Test
    public void testPaymentRequestSuccess() {
        PaymentRequest payment = PaymentRequest.builder()
                .senderName("John Doe")
                .senderPhone("0524999999")
                .receiverName("Jane Smith")
                .receiverPhone("0529111111")
                .amount(75.23)
                .build();

        PaymentResponse response = paymentSender.sendPayment(payment);

        paymentValidator.validate(response, payment);
        paymentValidator.validateTransactionId(response);
        paymentValidator.validateSqsData(response);
    }

    @Test
    public void testPaymentRequestWithDifferentAmount() {
        PaymentRequest payment = PaymentRequest.builder()
                .senderName("Alice Johnson")
                .senderPhone("0505555555")
                .receiverName("Bob Wilson")
                .receiverPhone("0506666666")
                .amount(150.50)
                .build();

        PaymentResponse response = paymentSender.sendPayment(payment);

        paymentValidator.validate(response, payment);
        paymentValidator.compareTransactionDetails(response, payment);
        paymentValidator.validateSqsData(response);
    }

    @Test
    public void testPaymentRequestWithSmallAmount() {
        PaymentRequest payment = PaymentRequest.builder()
                .senderName("Charlie Brown")
                .senderPhone("0507777777")
                .receiverName("Diana Prince")
                .receiverPhone("0508888888")
                .amount(10.00)
                .build();

        PaymentResponse response = paymentSender.sendPayment(payment);

        paymentValidator.validateTransactionId(response);
        paymentValidator.compareTransactionDetails(response, payment);
        paymentValidator.validateSqsData(response);
    }
}
