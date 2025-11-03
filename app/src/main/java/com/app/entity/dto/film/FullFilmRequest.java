package com.app.entity.dto.film;

import com.app.entity.Category;
import com.app.entity.Review;
import com.app.entity.dto.ReviewRequest;

import java.util.List;

public record FullFilmRequest(
        Long id,
        String title,
        Integer releaseYear,
        Integer duration,
        List<Category> categories,
        Float averageRating,
        Integer reviewCount,
        List<ReviewRequest> reviews
) {
}
