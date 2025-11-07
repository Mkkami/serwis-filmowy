package com.app.entity.dto.series;

import com.app.entity.Category;
import com.app.entity.dto.review.ReviewRequest;

import java.util.List;

public record FullSeriesRequest(
        Long id,
        String title,
        Integer releaseYear,
        Integer endYear,
        List<Category> categories,
        Float averageRating,
        Integer reviewCount,
        List<ReviewRequest> reviews
) {
}
