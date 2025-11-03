package com.app.service;

import com.app.entity.Review;
import com.app.entity.User;
import com.app.entity.dto.review.NewReviewRequest;
import com.app.entity.dto.review.ReviewRequest;
import com.app.repository.ReviewRepository;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    public Review createReview(NewReviewRequest reviewRequest, String username) {
        Review review = new Review();
        review.setComment(reviewRequest.comment());
        review.setRating(reviewRequest.rating());

        User user = userRepository.findByUsername(username);
        review.setUser(user);

        reviewRepository.save(review);
        return review;
    }
}
