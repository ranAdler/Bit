package com.example.bit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

    @Value("${sqs.region}")
    private String region;

    @Value("${aws.access.key.id:}")
    private String awsAccessKeyId;

    @Value("${aws.secret.access.key:}")
    private String awsSecretAccessKey;

    @Bean
    public SqsClient sqsClient() {
        AwsCredentialsProvider credentialsProvider = getCredentialsProvider();
        return SqsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    private AwsCredentialsProvider getCredentialsProvider() {
        // If credentials are provided via environment variables (from Jenkins secrets),
        // use StaticCredentialsProvider. Otherwise, use DefaultCredentialsProvider
        // which checks: env vars, system properties, .aws/credentials, IAM roles, etc.
        if (awsAccessKeyId != null && !awsAccessKeyId.isEmpty() &&
                awsSecretAccessKey != null && !awsSecretAccessKey.isEmpty()) {
            return StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey)
            );
        }
        return DefaultCredentialsProvider.create();
    }
}