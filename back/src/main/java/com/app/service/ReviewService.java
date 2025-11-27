package com.app.service;

import com.app.entity.Film;
import com.app.entity.Review;
import com.app.entity.Series;
import com.app.entity.User;
import com.app.entity.dto.review.NewReviewRequest;
import com.app.entity.dto.review.ReviewRequest;
import com.app.repository.ReviewRepository;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    public Review createReview(List<Review> reviewList, NewReviewRequest reviewRequest, String username) {
        User user = userRepository.findByUsername(username);
        boolean alreadyReviewed = reviewList.stream()
                .anyMatch(r -> r.getUser().getId().equals(user.getId()));
        if (alreadyReviewed) {
            throw new IllegalStateException("User already reviewed this film");
        }

        Review review = new Review();
        review.setComment(reviewRequest.comment());

        Integer rating = reviewRequest.rating();
        if (reviewRequest.rating() < 0) {
            rating = 0;
        } else if (reviewRequest.rating() > 10) {
            rating = 10;
        }

        review.setRating(rating);

        review.setUser(user);

        return review;
    }

    public Review addReviewToFilm(Film film, NewReviewRequest reviewRequest, String username) {
        return createReview(film.getReviews(), reviewRequest, username);
    }

    public Review addReviewToSeries(Series series, NewReviewRequest reviewRequest, String username) {
        return createReview(series.getReviews(), reviewRequest, username);
    }

    public void deleteReview(Long reviewId, String username) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new com.app.exception.ReviewNotFoundException(reviewId));
        
        User user = userRepository.findByUsername(username);
        
        // Check if the user is the owner of the review
        if (!review.getUser().getId().equals(user.getId())) {
            throw new com.app.exception.UnauthorizedReviewDeletionException();
        }
        
        reviewRepository.delete(review);
    }
}
