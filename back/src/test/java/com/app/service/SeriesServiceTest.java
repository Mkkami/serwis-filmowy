package com.app.service;

import com.app.entity.Category;
import com.app.entity.Episode;
import com.app.entity.Review;
import com.app.entity.Series;
import com.app.entity.dto.episode.NewEpisodeRequest;
import com.app.entity.dto.review.NewReviewRequest;
import com.app.entity.dto.series.CreateSeriesRequest;
import com.app.entity.dto.series.FullSeriesRequest;
import com.app.exception.EpisodeAlreadyExistsException;
import com.app.exception.SeriesNotFoundException;
import com.app.repository.SeriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeriesServiceTest {

    @Mock
    private SeriesRepository seriesRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private SeriesService seriesService;

    private Series testSeries;
    private Category testCategory;
    private CreateSeriesRequest createSeriesRequest;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Action");

        testSeries = new Series();
        testSeries.setId(1L);
        testSeries.setTitle("Test Series");
        testSeries.setReleaseYear(2024);
        testSeries.setCategories(Arrays.asList(testCategory));
        testSeries.setEpisodes(new ArrayList<>());
        testSeries.setReviews(new ArrayList<>());

        createSeriesRequest = new CreateSeriesRequest(
                "New Series",
                2024,
                2025,
                Arrays.asList("Action")
        );
    }

    @Test
    void getAllSeries_ShouldReturnAllSeries() {
        // Given
        when(seriesRepository.getAll()).thenReturn(Arrays.asList(testSeries));

        // When
        List<Series> result = seriesService.getAllSeries();

        // Then
        assertThat(result).hasSize(1);
        verify(seriesRepository, times(1)).getAll();
    }

    @Test
    void getSeries_WhenExists_ShouldReturnSeries() {
        // Given
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(testSeries));

        // When
        FullSeriesRequest result = seriesService.getSeries(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Test Series");
        verify(seriesRepository, times(1)).findById(1L);
    }

    @Test
    void getSeries_WhenNotExists_ShouldThrowException() {
        // Given
        when(seriesRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> seriesService.getSeries(999L))
                .isInstanceOf(SeriesNotFoundException.class);
    }

    @Test
    void createSeries_ShouldSaveAndReturnSeries() {
        // Given
        when(categoryService.get("Action")).thenReturn(testCategory);
        when(seriesRepository.save(any(Series.class))).thenReturn(testSeries);

        // When
        Series result = seriesService.createSeries(createSeriesRequest);

        // Then
        assertThat(result).isNotNull();
        verify(seriesRepository, times(1)).save(any(Series.class));
    }

    @Test
    void updateSeries_WhenExists_ShouldUpdateAndReturn() {
        // Given
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(testSeries));
        when(categoryService.get("Action")).thenReturn(testCategory);
        when(seriesRepository.save(any(Series.class))).thenReturn(testSeries);

        CreateSeriesRequest updateRequest = new CreateSeriesRequest(
                "Updated Series",
                2025,
                2026,
                Arrays.asList("Action")
        );

        // When
        Series result = seriesService.updateSeries(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(seriesRepository, times(1)).save(any(Series.class));
    }

    @Test
    void updateSeries_WhenNotExists_ShouldThrowException() {
        // Given
        when(seriesRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> seriesService.updateSeries(999L, createSeriesRequest))
                .isInstanceOf(SeriesNotFoundException.class);
    }

    @Test
    void addReview_ShouldAddReviewToSeries() {
        // Given
        NewReviewRequest reviewRequest = new NewReviewRequest(5, "Great!");
        Review review = new Review();
        review.setRating(5);
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(testSeries));
        when(reviewService.addReviewToSeries(eq(testSeries), eq(reviewRequest), eq("user"))).thenReturn(review);

        // When
        seriesService.addReview(1L, reviewRequest, "user");

        // Then
        verify(seriesRepository, times(1)).save(testSeries);
        assertThat(testSeries.getReviews()).contains(review);
    }

    @Test
    void getAllEpisodes_ShouldReturnEpisodes() {
        // Given
        Episode episode = new Episode();
        testSeries.addEpisode(episode);
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(testSeries));

        // When
        List<Episode> result = seriesService.getAllEpisodes(1L);

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    void addEpisode_ShouldAddEpisode() {
        // Given
        NewEpisodeRequest episodeRequest = new NewEpisodeRequest(1, "Pilot", LocalDate.now());
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(testSeries));

        // When
        seriesService.addEpisode(1L, episodeRequest);

        // Then
        verify(seriesRepository, times(1)).save(testSeries);
        assertThat(testSeries.getEpisodes()).hasSize(1);
    }

    @Test
    void addEpisode_WhenEpisodeExists_ShouldThrowException() {
        // Given
        Episode existingEpisode = new Episode();
        existingEpisode.setEpisodeNumber(1);
        testSeries.addEpisode(existingEpisode);
        
        NewEpisodeRequest episodeRequest = new NewEpisodeRequest(1, "Pilot", LocalDate.now());
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(testSeries));

        // When & Then
        assertThatThrownBy(() -> seriesService.addEpisode(1L, episodeRequest))
                .isInstanceOf(EpisodeAlreadyExistsException.class);
    }
}
