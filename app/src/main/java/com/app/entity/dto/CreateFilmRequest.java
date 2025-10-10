package com.app.entity.dto;

import com.app.entity.Category;

public record CreateFilmRequest(
    String title,
    String category,
    Integer duration,
    Integer releaseYear
) {}
