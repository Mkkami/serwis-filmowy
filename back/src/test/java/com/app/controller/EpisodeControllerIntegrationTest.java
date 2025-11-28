package com.app.controller;

import com.app.entity.Episode;
import com.app.entity.Series;
import com.app.entity.dto.episode.NewEpisodeRequest;
import com.app.repository.EpisodeRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EpisodeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAll_WhenSeriesExists_ShouldReturnEpisodes() throws Exception {
        Series series = new Series();
        series.setTitle("Test Series");
        series.setReleaseYear(2024);
        series.setEpisodes(new ArrayList<>());
        Series savedSeries = seriesRepository.save(series);

        Episode ep = new Episode();
        ep.setTitle("Pilot");
        ep.setEpisodeNumber(1);
        ep.setReleaseDate(LocalDate.of(2024,1,1));
        ep.setSeries(savedSeries);
        savedSeries.getEpisodes().add(ep);
        seriesRepository.save(savedSeries);

        mockMvc.perform(get("/series/" + savedSeries.getId() + "/episodes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void createEpisode_ShouldAddEpisode() throws Exception {
        Series series = new Series();
        series.setTitle("New Series");
        series.setReleaseYear(2025);
        series.setEpisodes(new ArrayList<>());
        Series savedSeries = seriesRepository.save(series);

        NewEpisodeRequest req = new NewEpisodeRequest(1, "Pilot", LocalDate.of(2025, 2, 1));

        mockMvc.perform(post("/series/" + savedSeries.getId() + "/episode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Episode has been added"));

        long count = episodeRepository.findAll().stream()
                .filter(e -> e.getSeries() != null && e.getSeries().getId().equals(savedSeries.getId()))
                .count();

        assertThat(count).isEqualTo(1);
    }

    @Test
    @WithMockUser
    void deleteEpisode_WhenExists_ShouldReturnNoContent() throws Exception {
        Series series = new Series();
        series.setTitle("Del Series");
        series.setReleaseYear(2020);
        series.setEpisodes(new ArrayList<>());
        Series savedSeries = seriesRepository.save(series);

        Episode ep = new Episode();
        ep.setTitle("ToDelete");
        ep.setEpisodeNumber(5);
        ep.setReleaseDate(LocalDate.of(2020,5,5));
        ep.setSeries(savedSeries);
        savedSeries.getEpisodes().add(ep);
        seriesRepository.save(savedSeries);
        // retrieve the persisted episode id
        Long epId = savedSeries.getEpisodes().get(0).getId();

        mockMvc.perform(delete("/series/" + savedSeries.getId() + "/episode/" + epId))
                .andExpect(status().isNoContent());

        Series updatedSeries = seriesRepository.findById(savedSeries.getId()).get();
        assertThat(updatedSeries.getEpisodes()).isEmpty();
    }
}
