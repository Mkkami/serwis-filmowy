package com.app.controller;

import com.app.entity.Category;
import com.app.entity.Film;
import com.app.entity.dto.film.CreateFilmRequest;
import com.app.entity.dto.film.FullFilmRequest;
import com.app.exception.FilmNotFoundException;
import com.app.service.FilmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;

    private Film testFilm;
    private CreateFilmRequest createFilmRequest;
    private FullFilmRequest fullFilmRequest;

    @BeforeEach
    void setUp() {
        testFilm = new Film();
        testFilm.setId(1L);
        testFilm.setTitle("Test Film");
        testFilm.setDuration(120);
        testFilm.setReleaseYear(2024);

        createFilmRequest = new CreateFilmRequest(
                "Test Film",
                120,
                2024,
                Arrays.asList("Action", "Drama")
        );

        fullFilmRequest = new FullFilmRequest(
                1L,
                "Test Film",
                2024,
                120,
                Arrays.asList(),
                0.0,
                0,
                Arrays.asList()
        );
    }

    @Test
    @WithMockUser
    void getAllFilms_ShouldReturnListOfFilms() throws Exception {
        // Given
        List<Film> films = Arrays.asList(testFilm);
        when(filmService.getAllFilms()).thenReturn(films);

        // When & Then
        mockMvc.perform(get("/film"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Film"))
                .andExpect(jsonPath("$[0].duration").value(120));

        verify(filmService, times(1)).getAllFilms();
    }

    @Test
    @WithMockUser
    void getFilm_WhenExists_ShouldReturnFilm() throws Exception {
        // Given
        when(filmService.getFilm(1L)).thenReturn(fullFilmRequest);

        // When & Then
        mockMvc.perform(get("/film/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Film"))
                .andExpect(jsonPath("$.duration").value(120));

        verify(filmService, times(1)).getFilm(1L);
    }

    @Test
    @WithMockUser
    void getFilm_WhenNotExists_ShouldReturnBadRequest() throws Exception {
        // Given
        when(filmService.getFilm(999L)).thenThrow(new FilmNotFoundException(999L));

        // When & Then
        mockMvc.perform(get("/film/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Film not found"));

        verify(filmService, times(1)).getFilm(999L);
    }

    @Test
    @WithMockUser
    void createFilm_ShouldReturnCreatedFilm() throws Exception {
        // Given
        when(filmService.createFilm(any(CreateFilmRequest.class))).thenReturn(testFilm);

        // When & Then
        mockMvc.perform(post("/film")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFilmRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Film"))
                .andExpect(jsonPath("$.duration").value(120));

        verify(filmService, times(1)).createFilm(any(CreateFilmRequest.class));
    }

    @Test
    @WithMockUser
    void updateFilm_WhenExists_ShouldReturnUpdatedFilm() throws Exception {
        // Given
        Film updatedFilm = new Film();
        updatedFilm.setId(1L);
        updatedFilm.setTitle("Updated Film");
        updatedFilm.setDuration(150);
        updatedFilm.setReleaseYear(2024);

        when(filmService.updateFilm(eq(1L), any(CreateFilmRequest.class))).thenReturn(updatedFilm);

        // When & Then
        mockMvc.perform(put("/film/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFilmRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Film"))
                .andExpect(jsonPath("$.duration").value(150));

        verify(filmService, times(1)).updateFilm(eq(1L), any(CreateFilmRequest.class));
    }

    @Test
    @WithMockUser
    void createFilm_WithMalformedJson_ShouldReturnBadRequest() throws Exception {
        // Given
        String malformedJson = "{ \"title\": \"Test Film\", \"duration\": 120 "; // Missing closing brace

        // When & Then
        mockMvc.perform(post("/film")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void updateFilm_WhenNotExists_ShouldReturnBadRequest() throws Exception {
        // Given
        when(filmService.updateFilm(eq(999L), any(CreateFilmRequest.class)))
                .thenThrow(new FilmNotFoundException(999L));

        // When & Then
        mockMvc.perform(put("/film/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFilmRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Film not found"));

        verify(filmService, times(1)).updateFilm(eq(999L), any(CreateFilmRequest.class));
    }
}
