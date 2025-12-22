package com.example.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_email", columnList = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_categories",
            joinColumns = @JoinColumn(name = "user_id"),
            indexes = @Index(name = "idx_category", columnList = "category"))
    private List<String> subscribedCategories = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_channels",
            joinColumns = @JoinColumn(name = "user_id"),
            indexes = @Index(name = "idx_channel", columnList = "channel"))
    @Enumerated(EnumType.STRING)
    private List<Channel> channels = new ArrayList<>();
}

