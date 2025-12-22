package com.example.notification.controller;

import com.example.notification.model.MessageRequest;
import com.example.notification.service.MessageService;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/v1/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService service;

    @PostMapping()
    public ResponseEntity<String> send(@RequestBody MessageRequest request) {
        if(Objects.nonNull(request) && request.isValid()){
            service.send(request);
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
