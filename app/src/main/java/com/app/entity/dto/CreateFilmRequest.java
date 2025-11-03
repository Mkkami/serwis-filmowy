package com.app.entity.dto;

import com.app.entity.Category;

import java.util.List;

public record CreateFilmRequest(
    String title,
    Integer duration,
    Integer releaseYear,
    List<String> categories
) {}
