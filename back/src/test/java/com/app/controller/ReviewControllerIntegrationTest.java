package com.app.controller;

import com.app.entity.Film;
import com.app.entity.Review;
import com.app.entity.Series;
import com.app.entity.User;
import com.app.entity.dto.review.NewReviewRequest;
import com.app.repository.FilmRepository;
import com.app.repository.ReviewRepository;
import com.app.repository.SeriesRepository;
import com.app.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser")
    void reviewFilm_ShouldCreateReview() throws Exception {
        // create user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("pass");
        userRepository.save(user);

        // create film
        Film film = new Film();
        film.setTitle("Movie");
        film.setDuration(100);
        film.setReleaseYear(2020);
        film.setReviews(new ArrayList<>());
        film.setCategories(new ArrayList<>());
        Film saved = filmRepository.save(film);

        NewReviewRequest req = new NewReviewRequest(8, "Nice movie");

        mockMvc.perform(post("/film/" + saved.getId() + "/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Review has been added"));

        Film updated = filmRepository.findById(saved.getId()).get();
        assertThat(updated.getReviews()).hasSize(1);
        Review r = updated.getReviews().get(0);
        assertThat(r.getUser()).isNotNull();
        assertThat(r.getUser().getUsername()).isEqualTo("testuser");
    }

    @Test
    @WithMockUser(username = "testuser")
    void reviewFilm_WhenUserAlreadyReviewed_ShouldReturnBadRequest() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("pass");
        userRepository.save(user);

        Film film = new Film();
        film.setTitle("Movie");
        film.setDuration(90);
        film.setReleaseYear(2019);
        film.setReviews(new ArrayList<>());
        film.setCategories(new ArrayList<>());

        Review review = new Review();
        review.setRating(7);
        review.setComment("Good");
        review.setUser(user);
        film.getReviews().add(review);
        filmRepository.save(film);

        NewReviewRequest req = new NewReviewRequest(9, "Great");

        mockMvc.perform(post("/film/" + film.getId() + "/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User already reviewed this film"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void deleteFilmReview_WhenSuccess_ShouldReturnOk() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("pass");
        userRepository.save(user);

        Film film = new Film();
        film.setTitle("Movie");
        film.setDuration(100);
        film.setReleaseYear(2020);
        film.setReviews(new ArrayList<>());
        film.setCategories(new ArrayList<>());

        Review review = new Review();
        review.setRating(10);
        review.setComment("Excellent");
        review.setUser(user);
        film.getReviews().add(review);
        Film saved = filmRepository.save(film);
        Long reviewId = saved.getReviews().get(0).getId();

        mockMvc.perform(delete("/film/" + saved.getId() + "/review/" + reviewId))
                .andExpect(status().isOk())
                .andExpect(content().string("Review has been deleted"));

        assertThat(reviewRepository.findById(reviewId)).isEmpty();
    }

    @Test
    @WithMockUser(username = "otheruser")
    void deleteFilmReview_WhenUnauthorized_ShouldReturnForbidden() throws Exception {
        User owner = new User();
        owner.setUsername("owner");
        owner.setPassword("pass");
        userRepository.save(owner);

        User other = new User();
        other.setUsername("otheruser");
        other.setPassword("pass");
        userRepository.save(other);

        Film film = new Film();
        film.setTitle("Movie");
        film.setDuration(100);
        film.setReleaseYear(2020);
        film.setReviews(new ArrayList<>());
        film.setCategories(new ArrayList<>());

        Review review = new Review();
        review.setRating(8);
        review.setComment("Nice");
        review.setUser(owner);
        film.getReviews().add(review);
        Film saved = filmRepository.save(film);
        Long reviewId = saved.getReviews().get(0).getId();

        mockMvc.perform(delete("/film/" + saved.getId() + "/review/" + reviewId))
                .andExpect(status().isForbidden())
                .andExpect(content().string("You are not authorized to delete this review"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void reviewSeries_ShouldCreateReview() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("pass");
        userRepository.save(user);

        Series series = new Series();
        series.setTitle("Series");
        series.setReleaseYear(2021);
        series.setEpisodes(new ArrayList<>());
        series.setReviews(new ArrayList<>());
        series.setCategories(new ArrayList<>());
        Series saved = seriesRepository.save(series);

        NewReviewRequest req = new NewReviewRequest(6, "Not bad");

        mockMvc.perform(post("/series/" + saved.getId() + "/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Review has been added"));

        Series updated = seriesRepository.findById(saved.getId()).get();
        assertThat(updated.getReviews()).hasSize(1);
        Review r = updated.getReviews().get(0);
        assertThat(r.getUser()).isNotNull();
        assertThat(r.getUser().getUsername()).isEqualTo("testuser");
    }

    @Test
    @WithMockUser(username = "otheruser")
    void deleteSeriesReview_WhenUnauthorized_ShouldReturnForbidden() throws Exception {
        User owner = new User();
        owner.setUsername("owner");
        owner.setPassword("pass");
        userRepository.save(owner);

        User other = new User();
        other.setUsername("otheruser");
        other.setPassword("pass");
        userRepository.save(other);

        Series series = new Series();
        series.setTitle("Series");
        series.setReleaseYear(2021);
        series.setEpisodes(new ArrayList<>());
        series.setReviews(new ArrayList<>());
        series.setCategories(new ArrayList<>());

        Review review = new Review();
        review.setRating(7);
        review.setComment("Okay");
        review.setUser(owner);
        series.getReviews().add(review);
        Series saved = seriesRepository.save(series);
        Long reviewId = saved.getReviews().get(0).getId();

        mockMvc.perform(delete("/series/" + saved.getId() + "/review/" + reviewId))
                .andExpect(status().isForbidden())
                .andExpect(content().string("You are not authorized to delete this review"));
    }
}
