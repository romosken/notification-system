package com.example.notification.strategy;

import com.example.notification.model.Channel;
import com.example.notification.model.User;
import org.springframework.stereotype.Service;

@Service
public class EmailStrategy implements NotificationStrategy {
    public void send(User user, String msg) {
        System.out.println("[EMAIL] Sent to " + user.getEmail() + ": " + msg);
    }

    public Channel getChannel() {
        return Channel.EMAIL;
    }
}