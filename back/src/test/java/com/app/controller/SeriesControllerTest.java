package com.app.controller;

import com.app.entity.Series;
import com.app.entity.dto.series.CreateSeriesRequest;
import com.app.entity.dto.series.FullSeriesRequest;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeriesController.class)
class SeriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SeriesService seriesService;

    private Series testSeries;
    private CreateSeriesRequest createSeriesRequest;
    private FullSeriesRequest fullSeriesRequest;

    @BeforeEach
    void setUp() {
        testSeries = new Series();
        testSeries.setId(1L);
        testSeries.setTitle("Test Series");
        testSeries.setReleaseYear(2024);

        createSeriesRequest = new CreateSeriesRequest(
                "Test Series",
                2024,
                null,
                Arrays.asList("Action", "Drama")
        );

        fullSeriesRequest = new FullSeriesRequest(
                1L,
                "Test Series",
                2024,
                null,
                Arrays.asList(),
                0.0f,
                0,
                Arrays.asList()
        );
    }

    @Test
    @WithMockUser
    void getAllSeries_ShouldReturnListOfSeries() throws Exception {
        // Given
        List<Series> seriesList = Arrays.asList(testSeries);
        when(seriesService.getAllSeries()).thenReturn(seriesList);

        // When & Then
        mockMvc.perform(get("/series"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Series"))
                .andExpect(jsonPath("$[0].releaseYear").value(2024));

        verify(seriesService, times(1)).getAllSeries();
    }

    @Test
    @WithMockUser
    void getSeries_WhenExists_ShouldReturnSeries() throws Exception {
        // Given
        when(seriesService.getSeries(1L)).thenReturn(fullSeriesRequest);

        // When & Then
        mockMvc.perform(get("/series/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Series"))
                .andExpect(jsonPath("$.releaseYear").value(2024));

        verify(seriesService, times(1)).getSeries(1L);
    }

    @Test
    @WithMockUser
    void getSeries_WhenNotExists_ShouldReturnBadRequest() throws Exception {
        // Given
        when(seriesService.getSeries(999L)).thenThrow(new SeriesNotFoundException(999L));

        // When & Then
        mockMvc.perform(get("/series/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Series not found"));

        verify(seriesService, times(1)).getSeries(999L);
    }

    @Test
    @WithMockUser
    void createSeries_ShouldReturnCreatedSeries() throws Exception {
        // Given
        when(seriesService.createSeries(any(CreateSeriesRequest.class))).thenReturn(testSeries);

        // When & Then
        mockMvc.perform(post("/series")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSeriesRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Series"))
                .andExpect(jsonPath("$.releaseYear").value(2024));

        verify(seriesService, times(1)).createSeries(any(CreateSeriesRequest.class));
    }

    @Test
    @WithMockUser
    void updateSeries_WhenExists_ShouldReturnUpdatedSeries() throws Exception {
        // Given
        Series updatedSeries = new Series();
        updatedSeries.setId(1L);
        updatedSeries.setTitle("Updated Series");
        updatedSeries.setReleaseYear(2025);

        when(seriesService.updateSeries(eq(1L), any(CreateSeriesRequest.class))).thenReturn(updatedSeries);

        // When & Then
        mockMvc.perform(put("/series/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSeriesRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Series"))
                .andExpect(jsonPath("$.releaseYear").value(2025));

        verify(seriesService, times(1)).updateSeries(eq(1L), any(CreateSeriesRequest.class));
    }

    @Test
    @WithMockUser
    void updateSeries_WhenNotExists_ShouldReturnBadRequest() throws Exception {
        // Given
        when(seriesService.updateSeries(eq(999L), any(CreateSeriesRequest.class)))
                .thenThrow(new SeriesNotFoundException(999L));

        // When & Then
        mockMvc.perform(put("/series/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSeriesRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Series not found"));

        verify(seriesService, times(1)).updateSeries(eq(999L), any(CreateSeriesRequest.class));
    }
}
