package com.example.bit.tests;

import com.example.bit.config.TestApplicationConfig;
import com.example.bit.service.SqsService;
import com.example.bit.utils.PaymentSender;
import com.example.bit.validators.PaymentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;

@ContextConfiguration(classes = TestApplicationConfig.class)
public class BasePaymentTest extends AbstractTestNGSpringContextTests {

    @Autowired
    protected PaymentSender paymentSender;

    @Autowired
    protected PaymentValidator paymentValidator;

    @Autowired(required = false)
    protected SqsService sqsService;

    @BeforeClass
    public void cleanupSqsQueue() {
        if (sqsService != null) {
            sqsService.purgeQueue();
        }
    }
}