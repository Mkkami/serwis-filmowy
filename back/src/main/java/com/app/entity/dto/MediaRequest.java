package com.app.entity.dto;

import com.app.entity.Film;
import com.app.entity.Series;

import java.util.Optional;

public record MediaRequest(
        Long id,
        String mediaType,
        String title,
        Integer releaseYear,
        Double averageRating,
        Integer reviewCount
) {
    public static MediaRequest fromFilm(Film film) {
        Double avgRating = Optional.ofNullable(film.getAverageRating()).orElse(0.0);
        Integer reviewCount = Optional.ofNullable(film.getReviewCount()).orElse(0);

        return new MediaRequest(
                film.getId(),
                "FILM",
                film.getTitle(),
                film.getReleaseYear(),
                avgRating,
                reviewCount
        );
    }

    public static MediaRequest fromSeries(Series series) {
        Double avgRating = Optional.ofNullable(series.getAverageRating()).orElse(0.0);
        Integer reviewCount = Optional.ofNullable(series.getReviewCount()).orElse(0);

        return new MediaRequest(
                series.getId(),
                "SERIES",
                series.getTitle(),
                series.getReleaseYear(),
                avgRating,
                reviewCount
        );
    }
}
