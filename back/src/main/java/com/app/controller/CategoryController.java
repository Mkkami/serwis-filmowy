package com.app.controller;

import com.app.entity.Category;
import com.app.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @ApiResponse(responseCode = "200", description = "Found the categories", 
            content = { @Content(mediaType = "application/json", 
            array = @ArraySchema(schema = @Schema(implementation = Category.class))) })
    @GetMapping("category")
    public List<Category> getAll() {
        return categoryService.getAll();
    }
}
