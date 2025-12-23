package com.example.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notifications_user_id", columnList = "user_id"),
        @Index(name = "idx_notifications_channel_id", columnList = "channel_id"),
        @Index(name = "idx_notifications_sent_at", columnList = "sent_at")
})
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class NotificationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "channel_id")
    private ChannelEntity channel;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "sent_at", nullable = false)
    @Builder.Default
    private ZonedDateTime sentAt = ZonedDateTime.now(ZoneOffset.UTC);
}

