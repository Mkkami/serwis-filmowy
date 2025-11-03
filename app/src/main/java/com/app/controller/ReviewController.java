package com.app.controller;

import com.app.entity.dto.review.NewReviewRequest;
import com.app.entity.dto.review.ReviewRequest;
import com.app.service.FilmService;
import com.app.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController("review")
public class ReviewController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private SeriesService seriesService;

    @PostMapping("film/{id}")
    public ResponseEntity<?> reviewFilm(@PathVariable Long id, @RequestBody NewReviewRequest reviewRequest, Principal principal) {
        filmService.addReview(id, reviewRequest, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body("Review has been added");
    }

    @PostMapping("series/{id}")
    public ResponseEntity<?> reviewSeries(@PathVariable Long id, @RequestBody NewReviewRequest reviewRequest, Principal principal) {
        seriesService.addReview(id, reviewRequest, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body("Review has been added");
    }
}
