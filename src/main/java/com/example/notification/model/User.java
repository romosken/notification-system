package com.example.notification.model;
import java.util.List;

public record User(
        Long id,
        String name,
        String email,
        String phoneNumber,
        List<String> subscribedCategories,
        List<String> channels
) {}