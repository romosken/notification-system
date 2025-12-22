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
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_message_id", columnList = "message_id"),
        @Index(name = "idx_channel", columnList = "channel"),
        @Index(name = "idx_sent_at", columnList = "sent_at"),
        @Index(name = "idx_success", columnList = "success")
})
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "category", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private String category;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Channel channel;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "sent_at", nullable = false)
    private ZonedDateTime sentAt = ZonedDateTime.now(ZoneOffset.UTC);
}

