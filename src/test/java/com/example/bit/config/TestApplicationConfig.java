package com.example.bit.config;

import com.example.bit.validators.PaymentValidator;
import com.example.bit.utils.RequestSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
    @PropertySource("classpath:test.properties"),
    @PropertySource(value = "classpath:dev.properties", ignoreResourceNotFound = true)
})
@ComponentScan(basePackages = {"com.example.bit.utils", "com.example.bit.validators", "com.example.bit.service"})
@Import(SqsConfig.class)
public class TestApplicationConfig {

    @Value("${api.base.url}")
    private String baseUrl;

    @Bean
    public RequestSender requestSender() {
        return new RequestSender(baseUrl);
    }

    @Bean
    public PaymentValidator paymentValidator() {
        return new PaymentValidator();
    }
}