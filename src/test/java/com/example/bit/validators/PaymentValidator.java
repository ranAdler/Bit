package com.example.bit.validators;

import com.example.bit.listener.SoftAssertListener;
import com.example.bit.model.PaymentRequest;
import com.example.bit.model.PaymentResponse;
import com.example.bit.service.SqsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.asserts.SoftAssert;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.Optional;

@Component
public class PaymentValidator {

    @Autowired(required = false)
    private SqsService sqsService;

    public void validate(PaymentResponse response, PaymentRequest request) {
        SoftAssert softAssert = SoftAssertListener.getSoftAssert();

        softAssert.assertNotNull(response, "Payment response should not be null");
        softAssert.assertNotNull(response.getTransactionId(), "Transaction ID should not be null");
        softAssert.assertEquals(response.getAmount(), request.getAmount(), "Amount should match");
        softAssert.assertEquals(response.getSenderPhone(), request.getSenderPhone(), "Sender phone should match");
        softAssert.assertEquals(response.getReceiverPhone(), request.getReceiverPhone(), "Receiver phone should match");
    }

    public void compareTransactionDetails(PaymentResponse response, PaymentRequest request) {
        SoftAssert softAssert = SoftAssertListener.getSoftAssert();

        softAssert.assertEquals(response.getAmount(), request.getAmount(), "Amount mismatch");
        softAssert.assertEquals(response.getSenderPhone(), request.getSenderPhone(), "Sender phone mismatch");
        softAssert.assertEquals(response.getReceiverPhone(), request.getReceiverPhone(), "Receiver phone mismatch");
    }

    public void validateTransactionId(PaymentResponse response) {
        SoftAssert softAssert = SoftAssertListener.getSoftAssert();
        softAssert.assertNotNull(response.getTransactionId(), "Transaction ID is required");
        softAssert.assertFalse(response.getTransactionId().isEmpty(), "Transaction ID should not be empty");
    }

    public void validateSqsData(PaymentResponse response) {
        SoftAssert softAssert = SoftAssertListener.getSoftAssert();

        Optional<Message> messageOptional =
            sqsService.findMessageByContent(response.getTransactionId());

        softAssert.assertTrue(messageOptional.isPresent(),
                "Transaction ID not found in SQS queue after polling: " + response.getTransactionId());

        messageOptional.ifPresent(msg -> sqsService.deleteMessage(msg.receiptHandle()));
    }
}