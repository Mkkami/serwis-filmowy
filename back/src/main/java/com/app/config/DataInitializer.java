package com.app.config;

import com.app.entity.Category;
import com.app.entity.User;
import com.app.entity.dto.UserLoginRequest;
import com.app.entity.dto.film.CreateFilmRequest;
import com.app.repository.CategoryRepository;
import com.app.repository.UserRepository;
import com.app.service.FilmService;
import com.app.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer {
    private final CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FilmService filmService;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<String> genres = mapper.readValue(new File("src/main/resources/genres.json"), new TypeReference<List<String>>() {
            });
            genres.forEach(this::createCategoryIfNotExists);
        }catch(Exception e){

        }
    }

    @PostConstruct
    public void initUser() {
        userService.createUser(new UserLoginRequest("user", "password"));
    }

    @PostConstruct
    public void initFilm() {
        List<String> cat = new ArrayList<>();
        cat.add("Action");
        cat.add("Fantasy");
        filmService.createFilm(new CreateFilmRequest(
                "Amazing Film",
                120,
                2025,
                cat
        ));
    }

    private void createCategoryIfNotExists(String name) {
        if (!categoryRepository.existsByName(name)) {
            Category category = new Category();
            category.setName(name);
            categoryRepository.save(category);
        }
    }
}
