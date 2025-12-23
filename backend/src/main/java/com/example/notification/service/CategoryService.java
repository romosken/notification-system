package com.example.notification.service;

import com.example.notification.model.CategoryEntity;
import com.example.notification.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        log.info("Retrieving all categories");
        return categoryRepository.findAll()
                .stream()
                .map(CategoryEntity::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}

