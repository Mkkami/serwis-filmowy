package com.app.config;

import com.app.entity.Category;
import com.app.entity.User;
import com.app.entity.dto.UserLoginRequest;
import com.app.repository.CategoryRepository;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
    private final CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

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

    @PostConstruct
    public void initUser() {
        userService.createUser(new UserLoginRequest("user", "password"));
    }

    private void createCategoryIfNotExists(String name) {
        if (!categoryRepository.existsByName(name)) {
            Category category = new Category();
            category.setName(name);
            categoryRepository.save(category);
        }
    }
}
