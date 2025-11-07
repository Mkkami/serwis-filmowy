package com.app.entity.dto;

import com.app.entity.Film;
import com.app.entity.Review;
import com.app.entity.Series;
import com.app.entity.dto.film.FullFilmRequest;
import com.app.entity.dto.review.NewReviewRequest;
import com.app.entity.dto.review.ReviewRequest;
import com.app.entity.dto.series.FullSeriesRequest;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    public static ReviewRequest reviewToDto(Review review) {
        return new ReviewRequest(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getUser().getUsername()
        );
    }

    public static List<ReviewRequest> reviewToDto(List<Review> reviews) {
        return reviews.stream().map(DtoMapper::reviewToDto).collect(Collectors.toList());
    }

    public static FullFilmRequest filmToFullDto(Film film) {
        List<ReviewRequest> reviews = reviewToDto(film.getReviews());

        return new FullFilmRequest(
                film.getId(),
                film.getTitle(),
                film.getReleaseYear(),
                film.getDuration(),
                film.getCategories(),
                film.averageRating(),
                film.countRatings(),
                reviews
        );
    }

    public static FullSeriesRequest seriesToFullDto(Series series) {
        List<ReviewRequest> reviews = reviewToDto(series.getReviews());

        return new FullSeriesRequest(
                series.getId(),
                series.getTitle(),
                series.getReleaseYear(),
                series.getEndYear(),
                series.getCategories(),
                series.averageRating(),
                series.countRatings(),
                reviews
        );
    }
}
