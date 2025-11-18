package com.app.service;

import com.app.entity.Category;
import com.app.entity.Film;
import com.app.entity.dto.film.CreateFilmRequest;
import com.app.entity.dto.film.FullFilmRequest;
import com.app.exception.FilmNotFoundException;
import com.app.repository.FilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private FilmService filmService;

    private Film testFilm;
    private Category testCategory1;
    private Category testCategory2;
    private CreateFilmRequest createFilmRequest;

    @BeforeEach
    void setUp() {
        testCategory1 = new Category();
        testCategory1.setId(1L);
        testCategory1.setName("Action");

        testCategory2 = new Category();
        testCategory2.setId(2L);
        testCategory2.setName("Drama");

        testFilm = new Film();
        testFilm.setId(1L);
        testFilm.setTitle("Test Film");
        testFilm.setDuration(120);
        testFilm.setReleaseYear(2024);
        testFilm.setCategories(Arrays.asList(testCategory1, testCategory2));
        testFilm.setReviews(Arrays.asList());

        createFilmRequest = new CreateFilmRequest(
                "New Film",
                150,
                2024,
                Arrays.asList("Action", "Drama")
        );
    }

    @Test
    void getAllFilms_ShouldReturnAllFilms() {
        // Given
        List<Film> films = Arrays.asList(testFilm);
        when(filmRepository.getAll()).thenReturn(films);

        // When
        List<Film> result = filmService.getAllFilms();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Film");
        verify(filmRepository, times(1)).getAll();
    }

    @Test
    void getFilm_WhenExists_ShouldReturnFilm() {
        // Given
        when(filmRepository.findById(1L)).thenReturn(Optional.of(testFilm));

        // When
        FullFilmRequest result = filmService.getFilm(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Test Film");
        assertThat(result.duration()).isEqualTo(120);
        verify(filmRepository, times(1)).findById(1L);
    }

    @Test
    void getFilm_WhenNotExists_ShouldThrowException() {
        // Given
        when(filmRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> filmService.getFilm(999L))
                .isInstanceOf(FilmNotFoundException.class)
                .hasMessageContaining("999");

        verify(filmRepository, times(1)).findById(999L);
    }

    @Test
    void createFilm_ShouldSaveAndReturnFilm() {
        // Given
        when(categoryService.get("Action")).thenReturn(testCategory1);
        when(categoryService.get("Drama")).thenReturn(testCategory2);
        when(filmRepository.save(any(Film.class))).thenReturn(testFilm);

        // When
        Film result = filmService.createFilm(createFilmRequest);

        // Then
        assertThat(result).isNotNull();
        verify(categoryService, times(1)).get("Action");
        verify(categoryService, times(1)).get("Drama");
        verify(filmRepository, times(1)).save(any(Film.class));
    }

    @Test
    void createFilm_WithNegativeDuration_ShouldSetToZero() {
        // Given
        CreateFilmRequest invalidRequest = new CreateFilmRequest(
                "Film",
                -10,
                2024,
                Arrays.asList("Action")
        );
        when(categoryService.get("Action")).thenReturn(testCategory1);
        when(filmRepository.save(any(Film.class))).thenAnswer(invocation -> {
            Film film = invocation.getArgument(0);
            assertThat(film.getDuration()).isEqualTo(0);
            return film;
        });

        // When
        filmService.createFilm(invalidRequest);

        // Then
        verify(filmRepository, times(1)).save(any(Film.class));
    }

    @Test
    void updateFilm_WhenExists_ShouldUpdateAndReturn() {
        // Given
        when(filmRepository.findById(1L)).thenReturn(Optional.of(testFilm));
        when(categoryService.get("Action")).thenReturn(testCategory1);
        when(categoryService.get("Drama")).thenReturn(testCategory2);
        when(filmRepository.save(any(Film.class))).thenReturn(testFilm);

        CreateFilmRequest updateRequest = new CreateFilmRequest(
                "Updated Film",
                180,
                2025,
                Arrays.asList("Action", "Drama")
        );

        // When
        Film result = filmService.updateFilm(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(filmRepository, times(1)).findById(1L);
        verify(filmRepository, times(1)).save(any(Film.class));
    }

    @Test
    void updateFilm_WhenNotExists_ShouldThrowException() {
        // Given
        when(filmRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> filmService.updateFilm(999L, createFilmRequest))
                .isInstanceOf(FilmNotFoundException.class)
                .hasMessageContaining("999");

        verify(filmRepository, times(1)).findById(999L);
        verify(filmRepository, never()).save(any(Film.class));
    }

    @Test
    void updateFilm_WithNullTitle_ShouldNotUpdateTitle() {
        // Given
        String originalTitle = testFilm.getTitle();
        when(filmRepository.findById(1L)).thenReturn(Optional.of(testFilm));
        when(filmRepository.save(any(Film.class))).thenReturn(testFilm);

        CreateFilmRequest updateRequest = new CreateFilmRequest(
                null,
                180,
                2025,
                Arrays.asList("Action")
        );

        // When
        filmService.updateFilm(1L, updateRequest);

        // Then
        assertThat(testFilm.getTitle()).isEqualTo(originalTitle);
    }

    @Test
    void deleteFilm_WhenExists_ShouldDeleteFilm() {
        // Given
        when(filmRepository.findById(1L)).thenReturn(Optional.of(testFilm));
        doNothing().when(filmRepository).delete(testFilm);

        // When
        filmService.deleteFilm(1L);

        // Then
        verify(filmRepository, times(1)).findById(1L);
        verify(filmRepository, times(1)).delete(testFilm);
    }

    @Test
    void deleteFilm_WhenNotExists_ShouldThrowException() {
        // Given
        when(filmRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> filmService.deleteFilm(999L))
                .isInstanceOf(FilmNotFoundException.class)
                .hasMessageContaining("999");

        verify(filmRepository, times(1)).findById(999L);
        verify(filmRepository, never()).delete(any(Film.class));
    }
}
