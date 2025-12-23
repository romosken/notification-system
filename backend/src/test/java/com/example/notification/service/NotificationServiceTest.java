package com.example.notification.service;

import com.example.notification.dto.NotificationLogResponse;
import com.example.notification.model.CategoryEntity;
import com.example.notification.model.Channel;
import com.example.notification.model.ChannelEntity;
import com.example.notification.model.NotificationLogEntity;
import com.example.notification.model.UserEntity;
import com.example.notification.repository.NotificationLogRepository;
import com.example.notification.repository.UserRepository;
import com.example.notification.strategy.NotificationStrategy;
import com.example.notification.strategy.NotificationStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationLogRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationStrategyFactory strategyFactory;

    @InjectMocks
    private NotificationService notificationService;

    private UserEntity user;
    private CategoryEntity category;
    private ChannelEntity emailChannel;
    private ChannelEntity smsChannel;
    private NotificationStrategy emailStrategy;
    private NotificationStrategy smsStrategy;

    @BeforeEach
    void setUp() {
        category = new CategoryEntity(1L, "Sports");
        emailChannel = new ChannelEntity(1L, Channel.EMAIL);
        smsChannel = new ChannelEntity(2L, Channel.SMS);

        Set<CategoryEntity> categories = new HashSet<>();
        categories.add(category);

        Set<ChannelEntity> channels = new HashSet<>();
        channels.add(emailChannel);
        channels.add(smsChannel);

        user = new UserEntity();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhoneNumber("1234567890");
        user.setSubscribedCategories(categories);
        user.setChannels(channels);

        emailStrategy = mock(NotificationStrategy.class);
        smsStrategy = mock(NotificationStrategy.class);
    }

    @Test
    void processNotification_ShouldProcessNotificationForAllSubscribedUsers() {
        // Given
        String categoryName = "Sports";
        String message = "Test notification message";
        List<UserEntity> subscribedUsers = Arrays.asList(user);

        when(strategyFactory.getStrategy(Channel.EMAIL)).thenReturn(emailStrategy);
        when(strategyFactory.getStrategy(Channel.SMS)).thenReturn(smsStrategy);
        when(userRepository.findByCategory(categoryName)).thenReturn(subscribedUsers);
        when(notificationRepository.save(any(NotificationLogEntity.class))).thenAnswer(invocation -> {
            NotificationLogEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });
        doNothing().when(emailStrategy).send(any(UserEntity.class), eq(categoryName), eq(message));
        doNothing().when(smsStrategy).send(any(UserEntity.class), eq(categoryName), eq(message));

        // When
        notificationService.processNotification(categoryName, message);

        // Then
        verify(userRepository, times(1)).findByCategory(categoryName);
        verify(notificationRepository, times(2)).save(any(NotificationLogEntity.class));
        verify(emailStrategy, times(1)).send(eq(user), eq(categoryName), eq(message));
        verify(smsStrategy, times(1)).send(eq(user), eq(categoryName), eq(message));
    }

    @Test
    void processNotification_WhenNoUsersSubscribed_ShouldNotSendNotifications() {
        // Given
        String categoryName = "Finance";
        String message = "Test notification message";

        when(userRepository.findByCategory(categoryName)).thenReturn(List.of());

        // When
        notificationService.processNotification(categoryName, message);

        // Then
        verify(userRepository, times(1)).findByCategory(categoryName);
        verify(notificationRepository, never()).save(any(NotificationLogEntity.class));
        verify(strategyFactory, never()).getStrategy(any(Channel.class));
    }

    @Test
    void processNotification_ShouldSaveLogForEachChannel() {
        // Given
        String categoryName = "Sports";
        String message = "Test notification message";
        List<UserEntity> subscribedUsers = Arrays.asList(user);

        when(strategyFactory.getStrategy(Channel.EMAIL)).thenReturn(emailStrategy);
        when(strategyFactory.getStrategy(Channel.SMS)).thenReturn(smsStrategy);
        when(userRepository.findByCategory(categoryName)).thenReturn(subscribedUsers);
        when(notificationRepository.save(any(NotificationLogEntity.class))).thenAnswer(invocation -> {
            NotificationLogEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });
        doNothing().when(emailStrategy).send(any(UserEntity.class), eq(categoryName), eq(message));
        doNothing().when(smsStrategy).send(any(UserEntity.class), eq(categoryName), eq(message));

        ArgumentCaptor<NotificationLogEntity> logCaptor = ArgumentCaptor.forClass(NotificationLogEntity.class);

        // When
        notificationService.processNotification(categoryName, message);

        // Then
        verify(notificationRepository, times(2)).save(logCaptor.capture());
        List<NotificationLogEntity> savedLogs = logCaptor.getAllValues();

        assertEquals(2, savedLogs.size());
        assertEquals(user, savedLogs.get(0).getUser());
        assertEquals(category, savedLogs.get(0).getCategory());
        assertEquals(message, savedLogs.get(0).getMessage());
        assertNotNull(savedLogs.get(0).getSentAt());
    }

    @Test
    void getAllLogs_ShouldReturnPagedNotificationLogs() {
        // Given
        Pageable pageable = PageRequest.of(0, 5);
        NotificationLogEntity log1 = NotificationLogEntity.builder()
                .id(1L)
                .user(user)
                .category(category)
                .channel(emailChannel)
                .message("Message 1")
                .sentAt(ZonedDateTime.now())
                .build();
        NotificationLogEntity log2 = NotificationLogEntity.builder()
                .id(2L)
                .user(user)
                .category(category)
                .channel(smsChannel)
                .message("Message 2")
                .sentAt(ZonedDateTime.now())
                .build();

        Page<NotificationLogEntity> logPage = new PageImpl<>(Arrays.asList(log1, log2), pageable, 2);
        when(notificationRepository.findAllOrderBySentAtDesc(pageable)).thenReturn(logPage);

        // When
        Page<NotificationLogResponse> result = notificationService.getAllLogs(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(5, result.getSize());

        NotificationLogResponse response1 = result.getContent().get(0);
        assertEquals(1L, response1.getId());
        assertEquals(user.getId(), response1.getUserId());
        assertEquals(user.getName(), response1.getUserName());
        assertEquals(category.getName(), response1.getCategoryName());

        verify(notificationRepository, times(1)).findAllOrderBySentAtDesc(pageable);
    }

    @Test
    void getAllLogs_WhenNoLogsExist_ShouldReturnEmptyPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 5);
        Page<NotificationLogEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(notificationRepository.findAllOrderBySentAtDesc(pageable)).thenReturn(emptyPage);

        // When
        Page<NotificationLogResponse> result = notificationService.getAllLogs(pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(notificationRepository, times(1)).findAllOrderBySentAtDesc(pageable);
    }

    @Test
    void processNotification_WithMultipleUsers_ShouldProcessAllUsers() {
        // Given
        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setName("Jane Doe");
        user2.setEmail("jane@example.com");
        user2.setSubscribedCategories(new HashSet<>(Arrays.asList(category)));
        user2.setChannels(new HashSet<>(Arrays.asList(emailChannel)));

        String categoryName = "Sports";
        String message = "Test notification message";
        List<UserEntity> subscribedUsers = Arrays.asList(user, user2);

        when(strategyFactory.getStrategy(Channel.EMAIL)).thenReturn(emailStrategy);
        when(strategyFactory.getStrategy(Channel.SMS)).thenReturn(smsStrategy);
        when(userRepository.findByCategory(categoryName)).thenReturn(subscribedUsers);
        when(notificationRepository.save(any(NotificationLogEntity.class))).thenAnswer(invocation -> {
            NotificationLogEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });
        doNothing().when(emailStrategy).send(any(UserEntity.class), eq(categoryName), eq(message));
        doNothing().when(smsStrategy).send(any(UserEntity.class), eq(categoryName), eq(message));

        // When
        notificationService.processNotification(categoryName, message);

        // Then
        verify(userRepository, times(1)).findByCategory(categoryName);
        verify(notificationRepository, times(3)).save(any(NotificationLogEntity.class)); // 2 channels for user1, 1 channel for user2
        verify(emailStrategy, times(2)).send(any(UserEntity.class), eq(categoryName), eq(message));
        verify(smsStrategy, times(1)).send(any(UserEntity.class), eq(categoryName), eq(message));
    }
}

