package com.app.controller;

import com.app.entity.Episode;
import com.app.entity.dto.episode.NewEpisodeRequest;
import com.app.exception.EpisodeAlreadyExistsException;
import com.app.exception.EpisodeNotFoundException;
import com.app.exception.SeriesNotFoundException;
import com.app.service.SeriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EpisodeController {

    @Autowired
    private SeriesService seriesService;

    @Operation(summary = "Get all episodes for a series")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the episodes",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Episode.class)))),
            @ApiResponse(responseCode = "400", description = "Series not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @GetMapping("series/{id}/episodes")
    public ResponseEntity<?> getAll(@PathVariable Long id) {
        List<Episode> episodeList;
        try {
            episodeList = seriesService.getAllEpisodes(id);
        } catch (SeriesNotFoundException e) {
            return ResponseEntity.badRequest().body("Series not found");
        }
        return ResponseEntity.ok().body(episodeList);
    }

    @Operation(summary = "Create a new episode")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Episode created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Episode already exists or invalid data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PostMapping("series/{id}/episode")
    public ResponseEntity<?> createEpisode(@PathVariable Long id, @RequestBody NewEpisodeRequest episodeRequest) {
        try {
            seriesService.addEpisode(id,  episodeRequest);
        } catch (EpisodeAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Episode has been added");
    }

    @Operation(summary = "Delete an episode")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Episode deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Episode or Series not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("series/{seriesId}/episode/{episodeId}")
    public ResponseEntity<?> deleteEpisode(@PathVariable Long seriesId, @PathVariable Long episodeId) {
        try {
            seriesService.deleteEpisode(seriesId, episodeId);

        } catch (EpisodeNotFoundException | SeriesNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

    }
}
