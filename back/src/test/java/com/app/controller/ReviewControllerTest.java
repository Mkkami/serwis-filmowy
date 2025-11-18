package com.app.controller;

import com.app.entity.dto.review.NewReviewRequest;
import com.app.service.FilmService;
import com.app.service.SeriesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;

    @MockBean
    private SeriesService seriesService;

    private NewReviewRequest reviewRequest;

    @BeforeEach
    void setUp() {
        reviewRequest = new NewReviewRequest(
                5,
                "Great movie!"
        );
    }

    @Test
    @WithMockUser(username = "testuser")
    void reviewFilm_ShouldCreateReview() throws Exception {
        // Given
        doNothing().when(filmService).addReview(eq(1L), any(NewReviewRequest.class), eq("testuser"));

        // When & Then
        mockMvc.perform(post("/film/1/review")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Review has been added"));

        verify(filmService, times(1)).addReview(eq(1L), any(NewReviewRequest.class), eq("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void reviewFilm_WhenError_ShouldReturnBadRequest() throws Exception {
        // Given
        doThrow(new IllegalStateException("User already reviewed this film"))
                .when(filmService).addReview(eq(1L), any(NewReviewRequest.class), eq("testuser"));

        // When & Then
        mockMvc.perform(post("/film/1/review")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User already reviewed this film"));

        verify(filmService, times(1)).addReview(eq(1L), any(NewReviewRequest.class), eq("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void reviewSeries_ShouldCreateReview() throws Exception {
        // Given
        doNothing().when(seriesService).addReview(eq(1L), any(NewReviewRequest.class), eq("testuser"));

        // When & Then
        mockMvc.perform(post("/series/1/review")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Review has been added"));

        verify(seriesService, times(1)).addReview(eq(1L), any(NewReviewRequest.class), eq("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void reviewSeries_WhenError_ShouldReturnBadRequest() throws Exception {
        // Given
        doThrow(new IllegalStateException("User already reviewed this series"))
                .when(seriesService).addReview(eq(1L), any(NewReviewRequest.class), eq("testuser"));

        // When & Then
        mockMvc.perform(post("/series/1/review")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User already reviewed this series"));

        verify(seriesService, times(1)).addReview(eq(1L), any(NewReviewRequest.class), eq("testuser"));
    }
}
