package com.app.controller;

import com.app.entity.Category;
import com.app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("public")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("category")
    public List<Category> getAll() {
        return categoryService.getAll();
    }
}
