package com.example.notification.strategy;

import com.example.notification.model.Channel;
import com.example.notification.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsStrategy implements NotificationStrategy {
    public void send(UserEntity user, String category, String msg) {
        log.info("[SMS] Sent to {}: [{}] -> {}", user.getPhoneNumber(), category, msg);

    }

    public Channel getChannel() {
        return Channel.SMS;
    }
}