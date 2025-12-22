package com.example.notification.strategy;

import com.example.notification.model.User;
import org.springframework.stereotype.Service;

@Service
public class PushStrategy implements NotificationStrategy {
    public void send(User user, String msg) { System.out.println("[PUSH] To: " + user.name() + " | " + msg); }
    public String getChannelName() { return "Push Notification"; }
}