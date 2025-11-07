package com.app.controller;

import com.app.entity.Episode;
import com.app.entity.dto.episode.NewEpisodeRequest;
import com.app.exception.EpisodeAlreadyExistsException;
import com.app.exception.SeriesNotFoundException;
import com.app.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EpisodeController {

    @Autowired
    private SeriesService seriesService;

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

    @PostMapping("series/{id}/episode")
    public ResponseEntity<?> createEpisode(@PathVariable Long id, @RequestBody NewEpisodeRequest episodeRequest) {
        try {
            seriesService.addEpisode(id,  episodeRequest);
        } catch (EpisodeAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Episode has been added");
    }
}
