package com.app.controller;

import com.app.entity.Series;
import com.app.entity.dto.series.CreateSeriesRequest;
import com.app.repository.SeriesRepository;
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
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SeriesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllSeries_ShouldReturnListOfSeries() throws Exception {
        Series s1 = new Series();
        s1.setTitle("Series A");
        s1.setReleaseYear(2018);
        s1.setEpisodes(new ArrayList<>());
        s1.setReviews(new ArrayList<>());
        s1.setCategories(new ArrayList<>());
        seriesRepository.save(s1);

        Series s2 = new Series();
        s2.setTitle("Series B");
        s2.setReleaseYear(2019);
        s2.setEpisodes(new ArrayList<>());
        s2.setReviews(new ArrayList<>());
        s2.setCategories(new ArrayList<>());
        seriesRepository.save(s2);

        mockMvc.perform(get("/series"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..title", hasItems("Series A", "Series B")));
    }

    @Test
    @WithMockUser
    void getSeries_WhenExists_ShouldReturnSeries() throws Exception {
        Series series = new Series();
        series.setTitle("Single Series");
        series.setReleaseYear(2020);
        series.setEpisodes(new ArrayList<>());
        series.setReviews(new ArrayList<>());
        series.setCategories(new ArrayList<>());
        Series saved = seriesRepository.save(series);

        mockMvc.perform(get("/series/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Single Series"))
                .andExpect(jsonPath("$.releaseYear").value(2020));
    }

    @Test
    @WithMockUser
    void createSeries_ShouldReturnCreatedSeries() throws Exception {
        CreateSeriesRequest req = new CreateSeriesRequest("New Series", 2021, 2022, Arrays.asList("Action"));

        mockMvc.perform(post("/series")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Series"));
    }

    @Test
    @WithMockUser
    void updateSeries_WhenExists_ShouldReturnUpdatedSeries() throws Exception {
        Series series = new Series();
        series.setTitle("Old Series");
        series.setReleaseYear(2017);
        series.setEpisodes(new ArrayList<>());
        series.setReviews(new ArrayList<>());
        series.setCategories(new ArrayList<>());
        Series saved = seriesRepository.save(series);

        CreateSeriesRequest update = new CreateSeriesRequest("Updated Series", 2018, 2020, Arrays.asList("Drama"));

        mockMvc.perform(put("/series/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Series"))
                .andExpect(jsonPath("$.releaseYear").value(2018));
    }

    @Test
    @WithMockUser
    void deleteSeries_WhenExists_ShouldReturnNoContent() throws Exception {
        Series series = new Series();
        series.setTitle("Delete Series");
        series.setReleaseYear(2016);
        series.setEpisodes(new ArrayList<>());
        series.setReviews(new ArrayList<>());
        series.setCategories(new ArrayList<>());
        Series saved = seriesRepository.save(series);

        mockMvc.perform(delete("/series/" + saved.getId()))
                .andExpect(status().isNoContent());

        boolean exists = seriesRepository.findById(saved.getId()).isPresent();
        assertThat(exists).isFalse();
    }
}
