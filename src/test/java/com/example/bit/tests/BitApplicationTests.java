package com.example.bit.tests;

import com.example.bit.model.PaymentRequest;
import com.example.bit.model.PaymentResponse;
import com.example.bit.utils.HttpStatusCode;
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
