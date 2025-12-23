package com.example.notification.service;

import com.example.notification.model.CategoryEntity;
import com.example.notification.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private List<CategoryEntity> mockCategories;

    @BeforeEach
    void setUp() {
        CategoryEntity category1 = new CategoryEntity(1L, "Sports");
        CategoryEntity category2 = new CategoryEntity(2L, "Finance");
        CategoryEntity category3 = new CategoryEntity(3L, "Movies");
        mockCategories = Arrays.asList(category1, category2, category3);
    }

    @Test
    void getAllCategories_ShouldReturnSortedListOfCategoryNames() {
        // Given
        when(categoryRepository.findAll()).thenReturn(mockCategories);

        // When
        List<String> result = categoryService.getAllCategories();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Finance", result.get(0));
        assertEquals("Movies", result.get(1));
        assertEquals("Sports", result.get(2));
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getAllCategories_WhenNoCategoriesExist_ShouldReturnEmptyList() {
        // Given
        when(categoryRepository.findAll()).thenReturn(List.of());

        // When
        List<String> result = categoryService.getAllCategories();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getAllCategories_ShouldCallRepositoryOnce() {
        // Given
        when(categoryRepository.findAll()).thenReturn(mockCategories);

        // When
        categoryService.getAllCategories();

        // Then
        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }
}

