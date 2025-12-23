package com.example.notification.service;

import com.example.notification.dto.MessageRequest;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageService {
    private final SqsTemplate sqsTemplate;
    private final String messageQueue;

    public MessageService(SqsTemplate sqsTemplate,
                          @Value("${sqs.queues.message}") String messageQueue) {
        this.sqsTemplate = sqsTemplate;
        this.messageQueue = messageQueue;
    }

    public void send(MessageRequest message) {
        try {
            sqsTemplate.send(messageQueue, message);
            log.info("Message sent to queue! {}", message);
        } catch (Exception e) {
            log.error("Error while sending message ({}) to SQS: {}", message, e.getMessage());
            throw e;
        }
    }
}
