package com.app.controller;

import com.app.entity.dto.review.NewReviewRequest;
import com.app.entity.dto.review.ReviewRequest;
import com.app.exception.FilmNotFoundException;
import com.app.exception.ReviewNotFoundException;
import com.app.exception.SeriesNotFoundException;
import com.app.service.FilmService;
import com.app.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class ReviewController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private SeriesService seriesService;

    @PostMapping("film/{id}/review")
    public ResponseEntity<?> reviewFilm(@PathVariable Long id, @RequestBody NewReviewRequest reviewRequest, Principal principal) {
        try {
            filmService.addReview(id, reviewRequest, principal.getName());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Review has been added");
    }

    @PostMapping("series/{id}/review")
    public ResponseEntity<?> reviewSeries(@PathVariable Long id, @RequestBody NewReviewRequest reviewRequest, Principal principal) {
        try {
            seriesService.addReview(id, reviewRequest, principal.getName());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Review has been added");
    }

    @DeleteMapping("film/{filmId}/review/{reviewId}")
    public ResponseEntity<?> deleteFilmReview(@PathVariable Long filmId, @PathVariable Long reviewId, Principal principal) {
        try {
            filmService.deleteReview(filmId, reviewId, principal.getName());
            return ResponseEntity.ok("Review has been deleted");
        } catch (ReviewNotFoundException | FilmNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (com.app.exception.UnauthorizedReviewDeletionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("series/{seriesId}/review/{reviewId}")
    public ResponseEntity<?> deleteSeriesReview(@PathVariable Long seriesId, @PathVariable Long reviewId, Principal principal) {
        try {
            seriesService.deleteReview(seriesId, reviewId, principal.getName());
            return ResponseEntity.ok("Review has been deleted");
        } catch (ReviewNotFoundException | SeriesNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (com.app.exception.UnauthorizedReviewDeletionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
