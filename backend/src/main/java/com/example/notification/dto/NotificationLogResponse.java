package com.example.notification.dto;

import com.example.notification.model.CategoryEntity;
import com.example.notification.model.ChannelEntity;
import com.example.notification.model.NotificationLogEntity;
import com.example.notification.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLogResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String categoryName;
    private String channelName;
    private String message;
    private ZonedDateTime sentAt;

    public NotificationLogResponse(NotificationLogEntity entity) {
        UserEntity user = entity.getUser();
        CategoryEntity category = entity.getCategory();
        ChannelEntity channel = entity.getChannel();
        
        this.id = entity.getId();
        this.userId = user != null ? user.getId() : null;
        this.userName = user != null ? user.getName() : null;
        this.categoryName = category != null ? category.getName() : null;
        this.channelName = channel != null ? channel.getName().name() : null;
        this.message = entity.getMessage();
        this.sentAt = entity.getSentAt();
    }
}

