package com.app.repository;

import com.app.entity.Category;
import com.app.entity.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FilmRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FilmRepository filmRepository;

    private Film testFilm;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setName("Action");
        entityManager.persist(testCategory);

        testFilm = new Film();
        testFilm.setTitle("Test Film");
        testFilm.setDuration(120);
        testFilm.setReleaseYear(2024);
        testFilm.setCategories(Arrays.asList(testCategory));
    }

    @Test
    void save_ShouldPersistFilm() {
        // When
        Film savedFilm = filmRepository.save(testFilm);
        entityManager.flush();

        // Then
        assertThat(savedFilm.getId()).isNotNull();
        assertThat(savedFilm.getTitle()).isEqualTo("Test Film");
        assertThat(savedFilm.getDuration()).isEqualTo(120);
    }

    @Test
    void findById_WhenExists_ShouldReturnFilm() {
        // Given
        Film savedFilm = entityManager.persist(testFilm);
        entityManager.flush();

        // When
        Optional<Film> found = filmRepository.findById(savedFilm.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Film");
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        // When
        Optional<Film> found = filmRepository.findById(999L);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void getAll_ShouldReturnAllFilms() {
        // Given
        Film film1 = new Film();
        film1.setTitle("Film 1");
        film1.setDuration(100);
        film1.setReleaseYear(2023);

        Film film2 = new Film();
        film2.setTitle("Film 2");
        film2.setDuration(110);
        film2.setReleaseYear(2024);

        entityManager.persist(film1);
        entityManager.persist(film2);
        entityManager.flush();

        // When
        List<Film> films = filmRepository.getAll();

        // Then
        assertThat(films).hasSize(2);
        assertThat(films).extracting(Film::getTitle)
                .containsExactlyInAnyOrder("Film 1", "Film 2");
    }

    @Test
    void update_ShouldModifyFilm() {
        // Given
        Film savedFilm = entityManager.persist(testFilm);
        entityManager.flush();
        entityManager.clear();

        // When
        Film filmToUpdate = filmRepository.findById(savedFilm.getId()).orElseThrow();
        filmToUpdate.setTitle("Updated Film");
        filmToUpdate.setDuration(150);
        Film updatedFilm = filmRepository.save(filmToUpdate);
        entityManager.flush();

        // Then
        Film found = entityManager.find(Film.class, savedFilm.getId());
        assertThat(found.getTitle()).isEqualTo("Updated Film");
        assertThat(found.getDuration()).isEqualTo(150);
    }

    @Test
    void delete_ShouldRemoveFilm() {
        // Given
        Film savedFilm = entityManager.persist(testFilm);
        entityManager.flush();
        Long filmId = savedFilm.getId();

        // When
        filmRepository.delete(savedFilm);
        entityManager.flush();

        // Then
        Film found = entityManager.find(Film.class, filmId);
        assertThat(found).isNull();
    }

    @Test
    void findAll_ShouldReturnEmptyListWhenNoFilms() {
        // When
        List<Film> films = filmRepository.findAll();

        // Then
        assertThat(films).isEmpty();
    }

    @Test
    void save_WithCategories_ShouldPersistRelationship() {
        // Given
        Category category2 = new Category();
        category2.setName("Drama");
        entityManager.persist(category2);

        testFilm.setCategories(Arrays.asList(testCategory, category2));

        // When
        Film savedFilm = filmRepository.save(testFilm);
        entityManager.flush();
        entityManager.clear();

        // Then
        Film found = filmRepository.findById(savedFilm.getId()).orElseThrow();
        assertThat(found.getCategories()).hasSize(2);
        assertThat(found.getCategories()).extracting(Category::getName)
                .containsExactlyInAnyOrder("Action", "Drama");
    }
}
