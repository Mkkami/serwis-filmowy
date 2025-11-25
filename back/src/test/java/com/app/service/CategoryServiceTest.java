package com.app.service;

import com.app.entity.Category;
import com.app.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAll_ShouldReturnAllCategories() {
        // Given
        Category category = new Category();
        category.setName("Action");
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));

        // When
        List<Category> result = categoryService.getAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Action");
    }

    @Test
    void get_WhenCategoryExists_ShouldReturnIt() {
        // Given
        Category category = new Category();
        category.setName("Action");
        when(categoryRepository.existsByName("Action")).thenReturn(true);
        when(categoryRepository.findByName("Action")).thenReturn(category);

        // When
        Category result = categoryService.get("Action");

        // Then
        assertThat(result).isEqualTo(category);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void get_WhenCategoryNotExists_ShouldCreateAndReturnIt() {
        // Given
        when(categoryRepository.existsByName("New")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        // When
        Category result = categoryService.get("New");

        // Then
        assertThat(result.getName()).isEqualTo("New");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}
