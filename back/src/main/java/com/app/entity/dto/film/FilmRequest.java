package com.app.entity.dto.film;


public record FilmRequest(
        Long id,
        String title,
        Integer releaseYear,
        Double averageRating,
        Long numberOfReviews
) {
}
