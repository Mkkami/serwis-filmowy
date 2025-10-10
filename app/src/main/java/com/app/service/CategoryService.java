package com.app.service;

import com.app.entity.Category;
import com.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category get(String name) {
        if (categoryRepository.existsByName(name)) {
            return categoryRepository.findByName(name);
        }
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return category;
    }
}
