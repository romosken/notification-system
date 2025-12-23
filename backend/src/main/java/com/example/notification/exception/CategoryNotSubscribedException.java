package com.example.notification.exception;

public class CategoryNotSubscribedException extends RuntimeException {
    public CategoryNotSubscribedException(String message) {
        super(message);
    }
}
