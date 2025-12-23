package com.example.notification.controller;

import com.example.notification.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void getCategories_ShouldReturnOkWithCategoriesList() {
        // Given
        List<String> categories = Arrays.asList("Finance", "Movies", "Sports");
        when(categoryService.getAllCategories()).thenReturn(categories);

        // When
        ResponseEntity<List<String>> response = categoryController.getCategories();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals("Finance", response.getBody().get(0));
        assertEquals("Movies", response.getBody().get(1));
        assertEquals("Sports", response.getBody().get(2));
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategories_WhenNoCategoriesExist_ShouldReturnEmptyList() {
        // Given
        when(categoryService.getAllCategories()).thenReturn(List.of());

        // When
        ResponseEntity<List<String>> response = categoryController.getCategories();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategories_ShouldCallServiceOnce() {
        // Given
        List<String> categories = Arrays.asList("Sports");
        when(categoryService.getAllCategories()).thenReturn(categories);

        // When
        categoryController.getCategories();

        // Then
        verify(categoryService, times(1)).getAllCategories();
        verifyNoMoreInteractions(categoryService);
    }
}

