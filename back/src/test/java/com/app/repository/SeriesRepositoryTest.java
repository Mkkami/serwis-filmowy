package com.app.repository;

import com.app.entity.Category;
import com.app.entity.Series;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SeriesRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SeriesRepository seriesRepository;

    private Series testSeries;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setName("Sci-Fi");
        entityManager.persist(testCategory);

        testSeries = new Series();
        testSeries.setTitle("Test Series");
        testSeries.setReleaseYear(2024);
        testSeries.setEndYear(2025);
        testSeries.setCategories(new ArrayList<>(List.of(testCategory)));
        testSeries.setEpisodes(new ArrayList<>());
        testSeries.setReviews(new ArrayList<>());
    }

    @Test
    void save_ShouldPersistSeries() {
        // When
        Series savedSeries = seriesRepository.save(testSeries);
        entityManager.flush();

        // Then
        assertThat(savedSeries.getId()).isNotNull();
        assertThat(savedSeries.getTitle()).isEqualTo("Test Series");
        assertThat(savedSeries.getCategories()).hasSize(1);
    }

    @Test
    void findById_WhenSeriesExists_ShouldReturnSeries() {
        // Given
        Series persistedSeries = entityManager.persist(testSeries);
        entityManager.flush();

        // When
        Optional<Series> foundSeries = seriesRepository.findById(persistedSeries.getId());

        // Then
        assertThat(foundSeries).isPresent();
        assertThat(foundSeries.get().getTitle()).isEqualTo("Test Series");
    }

    @Test
    void findById_WhenSeriesDoesNotExist_ShouldReturnEmpty() {
        // When
        Optional<Series> foundSeries = seriesRepository.findById(999L);

        // Then
        assertThat(foundSeries).isEmpty();
    }

    @Test
    void getAll_ShouldReturnAllSeries() {
        // Given
        entityManager.persist(testSeries);
        
        Series anotherSeries = new Series();
        anotherSeries.setTitle("Another Series");
        anotherSeries.setReleaseYear(2023);
        anotherSeries.setCategories(new ArrayList<>());
        anotherSeries.setEpisodes(new ArrayList<>());
        anotherSeries.setReviews(new ArrayList<>());
        entityManager.persist(anotherSeries);
        
        entityManager.flush();

        // When
        List<Series> allSeries = seriesRepository.getAll();

        // Then
        assertThat(allSeries).hasSize(2);
        assertThat(allSeries).extracting(Series::getTitle)
                .containsExactlyInAnyOrder("Test Series", "Another Series");
    }

    @Test
    void update_ShouldUpdateSeriesDetails() {
        // Given
        Series persistedSeries = entityManager.persist(testSeries);
        entityManager.flush();

        // When
        persistedSeries.setTitle("Updated Title");
        persistedSeries.setEndYear(2026);
        Series updatedSeries = seriesRepository.save(persistedSeries);
        entityManager.flush();

        // Then
        Optional<Series> foundSeries = seriesRepository.findById(updatedSeries.getId());
        assertThat(foundSeries).isPresent();
        assertThat(foundSeries.get().getTitle()).isEqualTo("Updated Title");
        assertThat(foundSeries.get().getEndYear()).isEqualTo(2026);
    }

    @Test
    void delete_ShouldRemoveSeries() {
        // Given
        Series persistedSeries = entityManager.persist(testSeries);
        entityManager.flush();

        // When
        seriesRepository.delete(persistedSeries);
        entityManager.flush();

        // Then
        Optional<Series> foundSeries = seriesRepository.findById(persistedSeries.getId());
        assertThat(foundSeries).isEmpty();
    }
}
