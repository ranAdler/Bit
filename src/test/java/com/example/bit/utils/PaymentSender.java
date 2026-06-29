package com.example.bit.utils;

import com.example.bit.listener.SoftAssertListener;
import com.example.bit.model.PaymentRequest;
import com.example.bit.model.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.asserts.SoftAssert;

@Component
public class PaymentSender {

    @Autowired
    private RequestSender requestSender;

    @Value("${payment.endpoint}")
    private String paymentEndpoint;

    public PaymentResponse sendPayment(PaymentRequest paymentRequest) {
        return requestSender.post(paymentEndpoint, paymentRequest, PaymentResponse.class);
    }

    public PaymentResponse sendPayment(PaymentRequest paymentRequest, HttpStatusCode httpStatusCode ,String errorMessage) {
        PaymentResponse response = requestSender.post(paymentEndpoint, paymentRequest, PaymentResponse.class, httpStatusCode);
        SoftAssert softAssert = SoftAssertListener.getSoftAssert();
        softAssert.assertEquals(response.getMessage(), errorMessage, "Message mismatch");
        return response;
    }
}