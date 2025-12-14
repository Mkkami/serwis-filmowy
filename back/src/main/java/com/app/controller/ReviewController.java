package com.app.controller;

import com.app.entity.dto.review.NewReviewRequest;
import com.app.entity.dto.review.ReviewRequest;
import com.app.entity.dto.review.UpdateReviewRequest;
import com.app.exception.FilmNotFoundException;
import com.app.exception.ReviewNotFoundException;
import com.app.exception.SeriesNotFoundException;
import com.app.service.FilmService;
import com.app.service.SeriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Add a review to a film")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PostMapping("film/{id}/review")
    public ResponseEntity<?> reviewFilm(@PathVariable Long id, @RequestBody NewReviewRequest reviewRequest, Principal principal) {
        try {
            filmService.addReview(id, reviewRequest, principal.getName());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Review has been added");
    }

    @Operation(summary = "Add a review to a series")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PostMapping("series/{id}/review")
    public ResponseEntity<?> reviewSeries(@PathVariable Long id, @RequestBody NewReviewRequest reviewRequest, Principal principal) {
        try {
            seriesService.addReview(id, reviewRequest, principal.getName());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Review has been added");
    }

    @Operation(summary = "Delete a film review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Review or Film not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized deletion",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
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

    @Operation(summary = "Delete a series review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Review or Series not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized deletion",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
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

    @Operation(summary = "Update a film review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Review or Film not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized update",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PutMapping("film/{filmId}/review/{reviewId}")
    public ResponseEntity<?> updateFilmReview(@PathVariable Long filmId, @PathVariable Long reviewId, 
                                              @RequestBody UpdateReviewRequest updateRequest, Principal principal) {
        try {
            filmService.updateReview(filmId, reviewId, updateRequest, principal.getName());
            return ResponseEntity.ok("Review has been updated");
        } catch (ReviewNotFoundException | FilmNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (com.app.exception.UnauthorizedReviewDeletionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @Operation(summary = "Update a series review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Review or Series not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized update",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PutMapping("series/{seriesId}/review/{reviewId}")
    public ResponseEntity<?> updateSeriesReview(@PathVariable Long seriesId, @PathVariable Long reviewId, 
                                                @RequestBody UpdateReviewRequest updateRequest, Principal principal) {
        try {
            seriesService.updateReview(seriesId, reviewId, updateRequest, principal.getName());
            return ResponseEntity.ok("Review has been updated");
        } catch (ReviewNotFoundException | SeriesNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (com.app.exception.UnauthorizedReviewDeletionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
