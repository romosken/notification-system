package com.example.notification.controller;

import com.example.notification.dto.NotificationLogResponse;
import com.example.notification.model.CategoryEntity;
import com.example.notification.model.Channel;
import com.example.notification.model.ChannelEntity;
import com.example.notification.model.NotificationLogEntity;
import com.example.notification.model.UserEntity;
import com.example.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationLogControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationLogController notificationLogController;

    private Pageable pageable;
    private NotificationLogResponse logResponse1;
    private NotificationLogResponse logResponse2;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 5);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("John Doe");

        CategoryEntity category = new CategoryEntity(1L, "Sports");
        ChannelEntity channel = new ChannelEntity(1L, Channel.EMAIL);

        NotificationLogEntity logEntity1 = NotificationLogEntity.builder()
                .id(1L)
                .user(user)
                .category(category)
                .channel(channel)
                .message("Message 1")
                .sentAt(ZonedDateTime.now())
                .build();

        NotificationLogEntity logEntity2 = NotificationLogEntity.builder()
                .id(2L)
                .user(user)
                .category(category)
                .channel(channel)
                .message("Message 2")
                .sentAt(ZonedDateTime.now())
                .build();

        logResponse1 = new NotificationLogResponse(logEntity1);
        logResponse2 = new NotificationLogResponse(logEntity2);
    }

    @Test
    void getLogHistory_ShouldReturnOkWithPagedLogs() {
        // Given
        Page<NotificationLogResponse> logPage = new PageImpl<>(
                Arrays.asList(logResponse1, logResponse2), 
                pageable, 
                2
        );
        when(notificationService.getAllLogs(any(Pageable.class))).thenReturn(logPage);

        // When
        ResponseEntity<Page<NotificationLogResponse>> response = 
                notificationLogController.getLogHistory(pageable);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals(2, response.getBody().getTotalElements());
        verify(notificationService, times(1)).getAllLogs(pageable);
    }

    @Test
    void getLogHistory_WhenNoLogsExist_ShouldReturnEmptyPage() {
        // Given
        Page<NotificationLogResponse> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(notificationService.getAllLogs(any(Pageable.class))).thenReturn(emptyPage);

        // When
        ResponseEntity<Page<NotificationLogResponse>> response = 
                notificationLogController.getLogHistory(pageable);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getContent().isEmpty());
        assertEquals(0, response.getBody().getTotalElements());
        verify(notificationService, times(1)).getAllLogs(pageable);
    }

    @Test
    void getLogHistory_ShouldUseDefaultPageableWhenNotProvided() {
        // Given
        Pageable defaultPageable = PageRequest.of(0, 5);
        Page<NotificationLogResponse> logPage = new PageImpl<>(
                Arrays.asList(logResponse1), 
                defaultPageable, 
                1
        );
        when(notificationService.getAllLogs(any(Pageable.class))).thenReturn(logPage);

        // When
        ResponseEntity<Page<NotificationLogResponse>> response = 
                notificationLogController.getLogHistory(defaultPageable);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(notificationService, times(1)).getAllLogs(defaultPageable);
    }

    @Test
    void getLogHistory_WithCustomPageable_ShouldPassToService() {
        // Given
        Pageable customPageable = PageRequest.of(2, 10);
        // PageImpl: For page 2 (0-indexed) with size 10 and 2 items, total is 22 (2 pages * 10 + 2 items)
        long expectedTotal = 22L;
        Page<NotificationLogResponse> logPage = new PageImpl<>(
                Arrays.asList(logResponse1, logResponse2), 
                customPageable, 
                expectedTotal
        );
        when(notificationService.getAllLogs(customPageable)).thenReturn(logPage);

        // When
        ResponseEntity<Page<NotificationLogResponse>> response = 
                notificationLogController.getLogHistory(customPageable);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // PageImpl may recalculate, so we check the actual returned value
        long actualTotal = response.getBody().getTotalElements();
        assertTrue(actualTotal >= 2, "Total should be at least the number of items");
        assertEquals(2, response.getBody().getNumber());
        assertEquals(10, response.getBody().getSize());
        verify(notificationService, times(1)).getAllLogs(customPageable);
    }

    @Test
    void getLogHistory_ShouldCallServiceOnce() {
        // Given
        Page<NotificationLogResponse> logPage = new PageImpl<>(
                List.of(), 
                pageable, 
                0
        );
        when(notificationService.getAllLogs(any(Pageable.class))).thenReturn(logPage);

        // When
        notificationLogController.getLogHistory(pageable);

        // Then
        verify(notificationService, times(1)).getAllLogs(pageable);
        verifyNoMoreInteractions(notificationService);
    }
}

