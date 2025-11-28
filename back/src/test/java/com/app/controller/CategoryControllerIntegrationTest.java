package com.app.controller;

import com.app.entity.Category;
import com.app.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @WithMockUser
    void getAll_ShouldContainSavedCategories() throws Exception {
        // prepare data (will be rolled back after test because of @Transactional)
        Category category1 = new Category();
        category1.setName("Action");

        Category category2 = new Category();
        category2.setName("Drama");

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        mockMvc.perform(get("/public/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name", hasItems("Action", "Drama")));
    }
}
