package com.app.controller;

import com.app.entity.Episode;
import com.app.entity.dto.episode.NewEpisodeRequest;
import com.app.exception.EpisodeAlreadyExistsException;
import com.app.exception.SeriesNotFoundException;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EpisodeController.class)
class EpisodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SeriesService seriesService;

    private Episode testEpisode;
    private NewEpisodeRequest episodeRequest;

    @BeforeEach
    void setUp() {
        testEpisode = new Episode();
        testEpisode.setId(1L);
        testEpisode.setTitle("Pilot");
        testEpisode.setEpisodeNumber(1);
        testEpisode.setReleaseDate(LocalDate.of(2024, 1, 1));

        episodeRequest = new NewEpisodeRequest(
                1,
                "Pilot",
                LocalDate.of(2024, 1, 1)
        );
    }

    @Test
    @WithMockUser
    void getAll_WhenSeriesExists_ShouldReturnEpisodes() throws Exception {
        // Given
        List<Episode> episodes = Arrays.asList(testEpisode);
        when(seriesService.getAllEpisodes(1L)).thenReturn(episodes);

        // When & Then
        mockMvc.perform(get("/series/1/episodes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Pilot"))
                .andExpect(jsonPath("$[0].episodeNumber").value(1));

        verify(seriesService, times(1)).getAllEpisodes(1L);
    }

    @Test
    @WithMockUser
    void getAll_WhenSeriesNotExists_ShouldReturnBadRequest() throws Exception {
        // Given
        when(seriesService.getAllEpisodes(999L)).thenThrow(new SeriesNotFoundException(999L));

        // When & Then
        mockMvc.perform(get("/series/999/episodes"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Series not found"));

        verify(seriesService, times(1)).getAllEpisodes(999L);
    }

    @Test
    void getAll_WithoutUser_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/series/1/episodes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getAll_WithInvalidPathVariable_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/series/abc/episodes"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void createEpisode_ShouldAddEpisode() throws Exception {
        // Given
        doNothing().when(seriesService).addEpisode(eq(1L), any(NewEpisodeRequest.class));

        // When & Then
        mockMvc.perform(post("/series/1/episode")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Episode has been added"));

        verify(seriesService, times(1)).addEpisode(eq(1L), any(NewEpisodeRequest.class));
    }

    @Test
    @WithMockUser
    void createEpisode_WhenEpisodeExists_ShouldReturnBadRequest() throws Exception {
        // Given
        doThrow(new EpisodeAlreadyExistsException("1"))
                .when(seriesService).addEpisode(eq(1L), any(NewEpisodeRequest.class));

        // When & Then
        mockMvc.perform(post("/series/1/episode")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeRequest)))
                .andExpect(status().isBadRequest());

        verify(seriesService, times(1)).addEpisode(eq(1L), any(NewEpisodeRequest.class));
    }

    @Test
    void createEpisode_WithoutUser_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/series/1/episode")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void createEpisode_WithMalformedJson_ShouldReturnBadRequest() throws Exception {
        String invalidJson = "{ \"title\": \"Pilot\", "; // niedomknięty JSON

        mockMvc.perform(post("/series/1/episode")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void createEpisode_WithInvalidPathVariable_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/series/abc/episode")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void deleteEpisode_WhenExists_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(seriesService).deleteEpisode(1L, 1L);

        // When & Then
        mockMvc.perform(delete("/series/1/episode/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(seriesService, times(1)).deleteEpisode(1L, 1L);
    }

    @Test
    @WithMockUser
    void deleteEpisode_WhenSeriesNotExists_ShouldReturnBadRequest() throws Exception {
        // Given
        doThrow(new SeriesNotFoundException(999L)).when(seriesService).deleteEpisode(999L, 1L);

        // When & Then
        mockMvc.perform(delete("/series/999/episode/1")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Series with id: 999 not found"));

        verify(seriesService, times(1)).deleteEpisode(999L, 1L);
    }

    @Test
    @WithMockUser
    void deleteEpisode_WhenEpisodeNotExists_ShouldReturnBadRequest() throws Exception {
        // Given
        doThrow(new com.app.exception.EpisodeNotFoundException(999L)).when(seriesService).deleteEpisode(1L, 999L);

        // When & Then
        mockMvc.perform(delete("/series/1/episode/999")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Episode with id: 999 not found"));

        verify(seriesService, times(1)).deleteEpisode(1L, 999L);
    }
}
