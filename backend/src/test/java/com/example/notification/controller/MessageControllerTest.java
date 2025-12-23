package com.example.notification.controller;

import com.example.notification.dto.MessageRequest;
import com.example.notification.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    private MessageRequest validRequest;
    private MessageRequest invalidRequest;

    @BeforeEach
    void setUp() {
        validRequest = new MessageRequest();
        validRequest.setCategory("Sports");
        validRequest.setBody("Test message body");

        invalidRequest = new MessageRequest();
        invalidRequest.setCategory("");
        invalidRequest.setBody("Test message body");
    }

    @Test
    void send_WithValidRequest_ShouldReturnAccepted() {
        // Given
        doNothing().when(messageService).send(validRequest);

        // When
        ResponseEntity<Void> response = messageController.send(validRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(messageService, times(1)).send(validRequest);
    }

    @Test
    void send_WithNullRequest_ShouldReturnBadRequest() {
        // When
        ResponseEntity<Void> response = messageController.send(null);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(messageService, never()).send(any());
    }

    @Test
    void send_WithInvalidRequest_ShouldReturnBadRequest() {
        // When
        ResponseEntity<Void> response = messageController.send(invalidRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(messageService, never()).send(any());
    }

    @Test
    void send_WithNullCategory_ShouldReturnBadRequest() {
        // Given
        MessageRequest request = new MessageRequest();
        request.setCategory(null);
        request.setBody("Test body");

        // When
        ResponseEntity<Void> response = messageController.send(request);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(messageService, never()).send(any());
    }

    @Test
    void send_WithNullBody_ShouldReturnBadRequest() {
        // Given
        MessageRequest request = new MessageRequest();
        request.setCategory("Sports");
        request.setBody(null);

        // When
        ResponseEntity<Void> response = messageController.send(request);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(messageService, never()).send(any());
    }

    @Test
    void send_WithBlankCategory_ShouldReturnBadRequest() {
        // Given
        MessageRequest request = new MessageRequest();
        request.setCategory("   ");
        request.setBody("Test body");

        // When
        ResponseEntity<Void> response = messageController.send(request);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(messageService, never()).send(any());
    }

    @Test
    void send_WithBlankBody_ShouldReturnBadRequest() {
        // Given
        MessageRequest request = new MessageRequest();
        request.setCategory("Sports");
        request.setBody("   ");

        // When
        ResponseEntity<Void> response = messageController.send(request);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(messageService, never()).send(any());
    }

    @Test
    void send_WhenServiceThrowsException_ShouldPropagateException() {
        // Given
        RuntimeException serviceException = new RuntimeException("Service error");
        doThrow(serviceException).when(messageService).send(validRequest);

        // When & Then
        assertThrows(RuntimeException.class, () -> messageController.send(validRequest));
        verify(messageService, times(1)).send(validRequest);
    }
}

