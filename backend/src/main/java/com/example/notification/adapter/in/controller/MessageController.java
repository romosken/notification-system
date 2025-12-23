package com.example.notification.controller;

import com.example.notification.dto.MessageRequest;
import com.example.notification.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/v1/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageService service;

    @PostMapping()
    public ResponseEntity<Void> send(@RequestBody MessageRequest request) {
        log.info("Received Message: {}", request);
        if (Objects.nonNull(request) && request.isValid()) {
            service.send(request);
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
