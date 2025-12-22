package com.example.notification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class MessageRequest {
    private String category;
    private String body;

    public boolean isValid() {
        return Objects.nonNull(this.category) &&
                Objects.nonNull(this.body) &&
                !this.category.isBlank() &&
                !this.body.isBlank();
    }
}
