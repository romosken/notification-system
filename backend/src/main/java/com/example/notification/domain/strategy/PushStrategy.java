package com.example.notification.strategy;

import com.example.notification.model.Channel;
import com.example.notification.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PushStrategy implements NotificationStrategy {
    public void send(UserEntity user, String category, String msg) {
        log.info("[PUSH] Sent to {}: [{}] -> {}", user.getName(), category, msg);

    }

    public Channel getChannel() {
        return Channel.PUSH;
    }
}