package com.example.notification.service;

import com.example.notification.dto.MessageRequest;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {
    private final NotificationService notificationService;

    @SqsListener("message-queue")
    public void listen(MessageRequest request) {
        try {
            notificationService.processNotification(request.getCategory(), request.getBody());
        } catch (Exception e) {
            log.error("Error while consuming message!", e);
            throw e; //Avoid deleting the message from queue, after 5 tries it will go to dlq
        }
    }
}