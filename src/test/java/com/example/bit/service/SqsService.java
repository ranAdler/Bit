package com.example.bit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SqsService {

    private final SqsClient sqsClient;

    @Value("${sqs.queue.url}")
    private String queueUrl;

    @Value("${sqs.retry.max.attempts}")
    private int maxPollingAttempts;

    @Value("${sqs.retry.delay.ms}")
    private long pollingDelayMs;

    public SqsService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public List<Message> readMessages() {
        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(1)
                .build();

        return sqsClient.receiveMessage(receiveRequest).messages();
    }

    public Optional<Message> findMessageByContent(String searchContent) {
        int attempts = 0;

        while (attempts < maxPollingAttempts) {
            List<Message> messages = readMessages();

            if (messages == null || messages.isEmpty()) {
                attempts++;
                if (attempts < maxPollingAttempts) {
                    try {
                        Thread.sleep(pollingDelayMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Thread interrupted while polling SQS", e);
                    }
                }
                continue;
            }

            Optional<Message> found = messages.stream()
                    .filter(msg -> msg.body().contains(searchContent))
                    .findFirst();

            if (found.isPresent()) {
                return found;
            }

            attempts++;
            if (attempts < maxPollingAttempts) {
                try {
                    Thread.sleep(pollingDelayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrupted while polling SQS", e);
                }
            }
        }

        return Optional.empty();
    }

    public void deleteMessage(String receiptHandle) {
        sqsClient.deleteMessage(builder -> builder
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
        );
    }

    public void purgeQueue() {
        List<software.amazon.awssdk.services.sqs.model.Message> messages;
        do {
            messages = readMessages();
            if (messages != null && !messages.isEmpty()) {
                messages.forEach(msg -> deleteMessage(msg.receiptHandle()));
            }
        } while (messages != null && !messages.isEmpty());
    }
}