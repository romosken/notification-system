package com.example.notification.strategy;
import com.example.notification.model.User;

public interface NotificationStrategy {
    void send(User user, String message);
    String getChannelName();
}