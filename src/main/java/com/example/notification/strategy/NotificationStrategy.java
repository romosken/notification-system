package com.example.notification.strategy;

import com.example.notification.model.Channel;
import com.example.notification.model.UserEntity;

public interface NotificationStrategy {
    void send(UserEntity user, String category, String message);

    Channel getChannel();
}