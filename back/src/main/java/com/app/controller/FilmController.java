package com.app.controller;

import com.app.entity.Film;
import com.app.entity.dto.film.CreateFilmRequest;
import com.app.entity.dto.film.FilmRequest;
import com.app.entity.dto.film.FullFilmRequest;
import com.app.exception.FilmNotFoundException;
import com.app.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class FilmController {

    @Autowired
    private FilmService filmService;

    @Operation(summary = "Get all films")
    @ApiResponse(responseCode = "200", description = "Found the films", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Film.class))))
    @GetMapping("film")
    public List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    @Operation(summary = "Get a film by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the film", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullFilmRequest.class))),
            @ApiResponse(responseCode = "400", description = "Film not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @GetMapping("film/{id}")
    public ResponseEntity<?> getFilm(@PathVariable Long id) {
        FullFilmRequest film;
        try {
            film = filmService.getFilm(id);
        } catch (FilmNotFoundException e) {
            return ResponseEntity.badRequest().body("Film not found");
        }
        return ResponseEntity.ok().body(film);
    }

    @Operation(summary = "Add a new film")
    @ApiResponse(responseCode = "201", description = "Film created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Film.class)))
    @PostMapping("film")
    public ResponseEntity<Film> addFilm(@RequestBody CreateFilmRequest filmRequest) {
        Film film = filmService.createFilm(filmRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @Operation(summary = "Update a film")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Film updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Film.class))),
            @ApiResponse(responseCode = "400", description = "Film not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PutMapping("film/{id}")
    public ResponseEntity<?> updateFilm(@PathVariable Long id, @RequestBody CreateFilmRequest filmRequest) {
        Film updatedFilm;
        try {
            updatedFilm = filmService.updateFilm(id, filmRequest);
        } catch (FilmNotFoundException e) {
            return ResponseEntity.badRequest().body("Film not found");
        }
        return ResponseEntity.ok().body(updatedFilm);
    }

    @Operation(summary = "Patch a film")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Film patched", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Film.class))),
            @ApiResponse(responseCode = "400", description = "Film not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PatchMapping("film/{id}")
    public ResponseEntity<?> patchFilm(@PathVariable Long id, @RequestBody CreateFilmRequest filmRequest) {
        Film updatedFilm;
        try {
            updatedFilm = filmService.updateFilm(id, filmRequest);
        } catch (FilmNotFoundException e) {
            return ResponseEntity.badRequest().body("Film not found");
        }
        return ResponseEntity.ok().body(updatedFilm);
    }

    @Operation(summary = "Delete a film")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Film deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Film not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("film/{id}")
    public ResponseEntity<?> deleteFilm(@PathVariable Long id) {
        try {
            filmService.deleteFilm(id);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Film not found");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("film/search")
    public ResponseEntity<Page<FilmRequest>> search(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        int pageSize = 20;

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return ResponseEntity.ok().body(filmService.searchFilms(title, categories, pageable));
    }
}
