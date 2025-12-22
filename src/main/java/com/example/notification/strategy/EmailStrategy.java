package com.example.notification.strategy;

import com.example.notification.model.User;
import org.springframework.stereotype.Service;

@Service
public class EmailStrategy implements NotificationStrategy {
    public void send(User user, String msg) { System.out.println("[EMAIL] Sent to " + user.email() + ": " + msg); }
    public String getChannelName() { return "E-Mail"; }
}