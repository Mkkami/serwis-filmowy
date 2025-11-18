package com.app.controller;

import com.app.entity.Category;
import com.app.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private List<Category> testCategories;

    @BeforeEach
    void setUp() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Action");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Drama");

        testCategories = Arrays.asList(category1, category2);
    }

    @Test
    @WithMockUser
    void getAll_ShouldReturnAllCategories() throws Exception {
        // Given
        when(categoryService.getAll()).thenReturn(testCategories);

        // When & Then
        mockMvc.perform(get("/public/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Action"))
                .andExpect(jsonPath("$[1].name").value("Drama"))
                .andExpect(jsonPath("$.length()").value(2));

        verify(categoryService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    void getAll_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Given
        when(categoryService.getAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/public/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(categoryService, times(1)).getAll();
    }
}
