package com.app.entity.dto.film;

import com.app.entity.Category;

import java.util.List;

public record FilmRequest(
        Long id,
        String title,
        Integer releaseYear,
        Integer duration,
        List<Category> categories,
        Float averageRating,
        Integer numberOfReviews
) {
}
