package com.example.notification.model;

import com.example.notification.exception.CategoryNotSubscribedException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "users_categories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"),
            indexes = {
                    @Index(name = "idx_users_categories_category", columnList = "category_id"),
                    @Index(name = "idx_users_categories_user", columnList = "user_id")

            }
    )
    private Set<CategoryEntity> subscribedCategories = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "users_channels",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id"),
            indexes = {
                    @Index(name = "idx_users_channels_user", columnList = "user_id")
            }
    )
    private Set<ChannelEntity> channels = new HashSet<>();

    public CategoryEntity getCategoryEntity(String category) {
        Set<CategoryEntity> userCategories = this.getSubscribedCategories();
        return userCategories.stream()
                .filter(userCat -> Objects.equals(userCat.getName(), category))
                .findFirst()
                .orElseThrow(() -> new CategoryNotSubscribedException("User is not subscribed to category!"));
    }
}

