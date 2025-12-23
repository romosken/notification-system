package com.example.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels", indexes = {
        @Index(name = "idx_channels_name", columnList = "name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Channel name;
}
