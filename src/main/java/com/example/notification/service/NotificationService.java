package com.example.notification.service;

import com.example.notification.model.ChannelEntity;
import com.example.notification.model.NotificationLogEntity;
import com.example.notification.model.UserEntity;
import com.example.notification.repository.NotificationLogRepository;
import com.example.notification.repository.UserRepository;
import com.example.notification.strategy.NotificationStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationLogRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationStrategyFactory strategyFactory;


    @Transactional
    public void processNotification(String category, String message) {
        List<UserEntity> usersSubscribed = userRepository.findByCategory(category);
        for (UserEntity user : usersSubscribed) {
            sendNotificationToUserChannels(user, category, message);
        }
    }

    private void sendNotificationToUserChannels(UserEntity user, String category, String message) {
        for (ChannelEntity channel : user.getChannels()) {
            saveLog(user, category, channel, message);
            strategyFactory.getStrategy(channel.getName()).send(user, category, message);
        }
    }

    private void saveLog(UserEntity user, String category, ChannelEntity channel, String message) {
        NotificationLogEntity notificationLog = NotificationLogEntity.builder()
                .user(user)
                .category(user.getCategoryEntity(category))
                .channel(channel)
                .message(message)
                .build();
        notificationRepository.save(notificationLog);
    }
}