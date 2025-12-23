package com.example.notification.controller;

import com.example.notification.dto.NotificationLogResponse;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/logs")
@RequiredArgsConstructor
@Slf4j
public class NotificationLogController {
    private final NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<Page<NotificationLogResponse>> getLogHistory(
            @PageableDefault(size = 5, sort = "sentAt") Pageable pageable) {
        log.info("Retrieving notification log history with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());
        Page<NotificationLogResponse> logs = notificationService.getAllLogs(pageable);
        return ResponseEntity.ok(logs);
    }
}

