package com.app.controller;

import com.app.entity.Film;
import com.app.entity.dto.CreateFilmRequest;
import com.app.exception.FilmNotFoundException;
import com.app.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("/api/")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @GetMapping("film")
    public List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping("film")
    public ResponseEntity<Film> addFilm(@RequestBody CreateFilmRequest filmRequest) {
        Film film = filmService.createFilm(filmRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping("film/{id}")
    public ResponseEntity<?> updateFilm(@PathVariable Long id,@RequestBody CreateFilmRequest filmRequest) {
        Film updatedFilm;
        try {
            updatedFilm = filmService.updateFilm(id, filmRequest);
        } catch (FilmNotFoundException e) {
            return ResponseEntity.badRequest().body("Film not found");
        }
        return ResponseEntity.ok().body(updatedFilm);
    }
}
