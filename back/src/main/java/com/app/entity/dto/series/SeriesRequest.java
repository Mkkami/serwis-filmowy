package com.app.entity.dto.series;

import com.app.entity.Category;

import java.util.List;

public record SeriesRequest(
        Long id,
        String title,
        Integer releaseYear,
        Integer endYear,
        List<Category> categories,
        Double averageRating,
        Integer reviewCount
) {
}
