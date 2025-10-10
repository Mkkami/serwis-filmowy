package com.app.config;

import com.app.entity.Category;
import com.app.repository.CategoryRepository;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void init() {
        createCategoryIfNotExists("action");
        createCategoryIfNotExists("scifi");
        createCategoryIfNotExists("fantasy");
        createCategoryIfNotExists("comedy");
    }

    private void createCategoryIfNotExists(String name) {
        if (!categoryRepository.existsByName(name)) {
            Category category = new Category();
            category.setName(name);
            categoryRepository.save(category);
        }
    }
}
