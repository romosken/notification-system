package com.example.notification.strategy;

import com.example.notification.model.User;
import org.springframework.stereotype.Service;

@Service
public class SmsStrategy implements NotificationStrategy {
    public void send(User user, String msg) { System.out.println("[SMS] To: " + user.phoneNumber() + " | " + msg); }
    public String getChannelName() { return "SMS"; }
}