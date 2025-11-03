package com.app.service;

import com.app.entity.Category;
import com.app.entity.Film;
import com.app.entity.dto.CreateFilmRequest;
import com.app.exception.FilmNotFoundException;
import com.app.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CategoryService categoryService;

    public List<Film> getAllFilms() {
        return filmRepository.getAll();
    }

    public Film createFilm(CreateFilmRequest filmRequest) {
        Film film = new Film();
        film.setTitle(filmRequest.title());
        film.setDuration(filmRequest.duration() > 0 ? filmRequest.duration() : 0);
        film.setReleaseYear(filmRequest.releaseYear() > 0 ? filmRequest.releaseYear() : 0);
        List<Category> categories = filmRequest.categories().stream()
                .map(cat -> categoryService.get(cat))
                .collect(Collectors.toList());

        film.setCategories(categories);
        filmRepository.save(film);
        return film;
    }

    public Film updateFilm(Long id, CreateFilmRequest filmRequest) {
        Film film = filmRepository.findById(id).orElseThrow( () -> new FilmNotFoundException(id));
        if (filmRequest.title() != null && !filmRequest.title().isBlank()) {
            film.setTitle(filmRequest.title());
        }
        if (filmRequest.categories() != null && !filmRequest.categories().isEmpty()) {
            film.setCategories( filmRequest.categories().stream()
                    .map(cat -> categoryService.get(cat))
                    .collect(Collectors.toList()));
        }
        if (filmRequest.duration() != null && filmRequest.duration() > 0) {
            film.setDuration(filmRequest.duration());
        }
        if (filmRequest.releaseYear() != null && filmRequest.releaseYear() > 0) {
            film.setReleaseYear(filmRequest.releaseYear());
        }
        filmRepository.save(film);
        return film;
    }
}
