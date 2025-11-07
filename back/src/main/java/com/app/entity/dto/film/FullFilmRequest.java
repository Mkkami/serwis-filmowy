package com.app.entity.dto.film;

import com.app.entity.Category;
import com.app.entity.dto.review.ReviewRequest;

import java.util.List;

public record FullFilmRequest(
        Long id,
        String title,
        Integer releaseYear,
        Integer duration,
        List<Category> categories,
        Double averageRating,
        Integer reviewCount,
        List<ReviewRequest> reviews
) {
}
