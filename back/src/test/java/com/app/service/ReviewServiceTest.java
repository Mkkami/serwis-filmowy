package com.app.service;

import com.app.entity.Film;
import com.app.entity.Review;
import com.app.entity.User;
import com.app.entity.dto.review.NewReviewRequest;
import com.app.repository.ReviewRepository;
import com.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User testUser;
    private NewReviewRequest reviewRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        reviewRequest = new NewReviewRequest(5, "Great!");
    }

    @Test
    void createReview_ShouldCreateReview() {
        // Given
        List<Review> existingReviews = new ArrayList<>();
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // When
        Review result = reviewService.createReview(existingReviews, reviewRequest, "testuser");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRating()).isEqualTo(5);
        assertThat(result.getComment()).isEqualTo("Great!");
        assertThat(result.getUser()).isEqualTo(testUser);
    }

    @Test
    void createReview_WhenUserAlreadyReviewed_ShouldThrowException() {
        // Given
        Review existingReview = new Review();
        existingReview.setUser(testUser);
        List<Review> existingReviews = List.of(existingReview);
        
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // When & Then
        assertThatThrownBy(() -> reviewService.createReview(existingReviews, reviewRequest, "testuser"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User already reviewed this film");
    }

    @Test
    void createReview_WithRatingGreaterThan10_ShouldCapAt10() {
        // Given
        NewReviewRequest highRatingRequest = new NewReviewRequest(15, "Great!");
        List<Review> existingReviews = new ArrayList<>();
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // When
        Review result = reviewService.createReview(existingReviews, highRatingRequest, "testuser");

        // Then
        assertThat(result.getRating()).isEqualTo(10);
    }

    @Test
    void createReview_WithRatingLessThan0_ShouldCapAt0() {
        // Given
        NewReviewRequest lowRatingRequest = new NewReviewRequest(-5, "Bad!");
        List<Review> existingReviews = new ArrayList<>();
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // When
        Review result = reviewService.createReview(existingReviews, lowRatingRequest, "testuser");

        // Then
        assertThat(result.getRating()).isEqualTo(0);
    }

    @Test
    void addReviewToFilm_ShouldCallCreateReview() {
        // Given
        Film film = new Film();
        film.setReviews(new ArrayList<>());
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // When
        Review result = reviewService.addReviewToFilm(film, reviewRequest, "testuser");

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void deleteReview_WhenUserIsOwner_ShouldDelete() {
        // Given
        Review review = new Review();
        review.setId(1L);
        review.setUser(testUser);
        
        when(reviewRepository.findById(1L)).thenReturn(java.util.Optional.of(review));
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // When
        reviewService.deleteReview(1L, "testuser");

        // Then
        verify(reviewRepository).delete(review);
    }

    @Test
    void deleteReview_WhenReviewNotExists_ShouldThrowException() {
        // Given
        when(reviewRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reviewService.deleteReview(999L, "testuser"))
                .isInstanceOf(com.app.exception.ReviewNotFoundException.class);
        
        verify(reviewRepository, never()).delete(any(Review.class));
    }

    @Test
    void deleteReview_WhenUserIsNotOwner_ShouldThrowException() {
        // Given
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("other");

        Review review = new Review();
        review.setId(1L);
        review.setUser(otherUser);
        
        when(reviewRepository.findById(1L)).thenReturn(java.util.Optional.of(review));
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // When & Then
        assertThatThrownBy(() -> reviewService.deleteReview(1L, "testuser"))
                .isInstanceOf(com.app.exception.UnauthorizedReviewDeletionException.class);
        
        verify(reviewRepository, never()).delete(any(Review.class));
    }
}
