package com.example.notification.service;

import com.example.notification.model.Channel;
import com.example.notification.model.NotificationLog;
import com.example.notification.model.User;
import com.example.notification.repository.NotificationLogRepository;
import com.example.notification.repository.UserRepository;
import com.example.notification.strategy.NotificationStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationLogRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationStrategyFactory strategyFactory;


    public void processNotification(String category, String message) {
        List<User> usersSubscribed = userRepository.findBySubscribedCategory(category);
        for (User user : usersSubscribed) {
            sendNotificationToUserChannels(category, message, user);
        }
    }

    private void sendNotificationToUserChannels(String category, String message, User user) {
        for (Channel channel : user.getChannels()) {
            strategyFactory.getStrategy(channel).send(user, message);
            saveLog(user, category, channel, message);
        }
    }

    private void saveLog(User user, String category, Channel channel, String message) {
        NotificationLog notificationLog = NotificationLog.builder()
                .userId(user.getId())
                .category(category)
                .channel(channel)
                .message(message)
                .build();
        notificationRepository.save(notificationLog);
    }
}