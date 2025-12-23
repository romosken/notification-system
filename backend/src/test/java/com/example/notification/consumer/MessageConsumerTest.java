package com.example.notification.service;

import com.example.notification.dto.MessageRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MessageConsumer messageConsumer;

    private MessageRequest messageRequest;

    @BeforeEach
    void setUp() {
        messageRequest = new MessageRequest();
        messageRequest.setCategory("Sports");
        messageRequest.setBody("Test notification message");
    }

    @Test
    void listen_ShouldProcessNotificationSuccessfully() {
        // Given
        doNothing().when(notificationService).processNotification(
                eq("Sports"), 
                eq("Test notification message")
        );

        // When
        assertDoesNotThrow(() -> messageConsumer.listen(messageRequest));

        // Then
        verify(notificationService, times(1)).processNotification(
                "Sports", 
                "Test notification message"
        );
    }

    @Test
    void listen_WhenNotificationServiceThrowsException_ShouldPropagateException() {
        // Given
        RuntimeException serviceException = new RuntimeException("Processing failed");
        doThrow(serviceException).when(notificationService).processNotification(
                eq("Sports"), 
                eq("Test notification message")
        );

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, 
                () -> messageConsumer.listen(messageRequest));
        
        assertEquals("Processing failed", thrown.getMessage());
        verify(notificationService, times(1)).processNotification(
                "Sports", 
                "Test notification message"
        );
    }

    @Test
    void listen_WithDifferentCategory_ShouldProcessCorrectly() {
        // Given
        messageRequest.setCategory("Finance");
        messageRequest.setBody("Finance notification");
        doNothing().when(notificationService).processNotification(
                eq("Finance"), 
                eq("Finance notification")
        );

        // When
        assertDoesNotThrow(() -> messageConsumer.listen(messageRequest));

        // Then
        verify(notificationService, times(1)).processNotification(
                "Finance", 
                "Finance notification"
        );
    }

    @Test
    void listen_ShouldExtractCategoryAndBodyFromRequest() {
        // Given
        MessageRequest request = new MessageRequest();
        request.setCategory("Movies");
        request.setBody("New movie released");
        doNothing().when(notificationService).processNotification(
                eq("Movies"), 
                eq("New movie released")
        );

        // When
        messageConsumer.listen(request);

        // Then
        verify(notificationService, times(1)).processNotification(
                "Movies", 
                "New movie released"
        );
    }

    @Test
    void listen_WhenExceptionOccurs_ShouldNotCatchException() {
        // Given
        IllegalStateException exception = new IllegalStateException("Invalid state");
        doThrow(exception).when(notificationService).processNotification(
                anyString(), 
                anyString()
        );

        // When & Then
        assertThrows(IllegalStateException.class, 
                () -> messageConsumer.listen(messageRequest));
        
        verify(notificationService, times(1)).processNotification(
                anyString(), 
                anyString()
        );
    }
}

