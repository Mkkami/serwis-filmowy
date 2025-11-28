package com.app.controller;

import com.app.entity.Film;
import com.app.entity.dto.film.CreateFilmRequest;
import com.app.repository.FilmRepository;
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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FilmControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllFilms_ShouldReturnListOfFilms() throws Exception {
        Film f1 = new Film();
        f1.setTitle("Film A");
        f1.setReleaseYear(2020);
        f1.setDuration(100);
        f1.setReviews(new java.util.ArrayList<>());
        f1.setCategories(new java.util.ArrayList<>());
        filmRepository.save(f1);

        Film f2 = new Film();
        f2.setTitle("Film B");
        f2.setReleaseYear(2021);
        f2.setDuration(110);
        f2.setReviews(new java.util.ArrayList<>());
        f2.setCategories(new java.util.ArrayList<>());
        filmRepository.save(f2);

        mockMvc.perform(get("/film"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..title", hasItems("Film A", "Film B")));
    }

    @Test
    @WithMockUser
    void getFilm_WhenExists_ShouldReturnFilm() throws Exception {
        Film film = new Film();
        film.setTitle("Single Film");
        film.setReleaseYear(2022);
        film.setDuration(120);
        film.setReviews(new java.util.ArrayList<>());
        film.setCategories(new java.util.ArrayList<>());
        Film saved = filmRepository.save(film);

        mockMvc.perform(get("/film/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Single Film"))
                .andExpect(jsonPath("$.duration").value(120));
    }

    @Test
    @WithMockUser
    void createFilm_ShouldReturnCreatedFilm() throws Exception {
        CreateFilmRequest req = new CreateFilmRequest(
                "Create Film",
                90,
                2024,
                Arrays.asList("Action")
        );

        mockMvc.perform(post("/film")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Create Film"))
                .andExpect(jsonPath("$.duration").value(90));
    }

    @Test
    @WithMockUser
    void updateFilm_WhenExists_ShouldReturnUpdatedFilm() throws Exception {
        Film film = new Film();
        film.setTitle("Old Title");
        film.setReleaseYear(2020);
        film.setDuration(80);
        film.setReviews(new java.util.ArrayList<>());
        film.setCategories(new java.util.ArrayList<>());
        Film saved = filmRepository.save(film);

        CreateFilmRequest update = new CreateFilmRequest("Updated", 95, 2023, Arrays.asList("Drama"));

        mockMvc.perform(put("/film/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"))
                .andExpect(jsonPath("$.duration").value(95));
    }

    @Test
    @WithMockUser
    void deleteFilm_WhenExists_ShouldReturnNoContent() throws Exception {
        Film film = new Film();
        film.setTitle("ToBeDeleted");
        film.setReleaseYear(2019);
        film.setDuration(80);
        film.setReviews(new java.util.ArrayList<>());
        film.setCategories(new java.util.ArrayList<>());
        Film saved = filmRepository.save(film);

        mockMvc.perform(delete("/film/" + saved.getId()))
                .andExpect(status().isNoContent());

        boolean exists = filmRepository.findById(saved.getId()).isPresent();
        assertThat(exists).isFalse();
    }
}
