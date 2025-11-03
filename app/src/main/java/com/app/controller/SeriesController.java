package com.app.controller;

import com.app.entity.Series;
import com.app.entity.dto.series.CreateSeriesRequest;
import com.app.entity.dto.series.FullSeriesRequest;
import com.app.exception.SeriesNotFoundException;
import com.app.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @GetMapping("series")
    public ResponseEntity<List<Series>> getAllSeries() {
        return ResponseEntity.ok(seriesService.getAllSeries());
    }

    @GetMapping("series/{id}")
    public ResponseEntity<?> getSeries(@PathVariable Long id) {
        FullSeriesRequest series;
        try {
            series = seriesService.getSeries(id);
        } catch (SeriesNotFoundException e) {
            return ResponseEntity.badRequest().body("Series not found");
        }
        return ResponseEntity.ok().body(series);
    }

    @PostMapping("series")
    public ResponseEntity<Series> addSeries(@RequestBody CreateSeriesRequest seriesRequest) {
        Series series = seriesService.createSeries(seriesRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(series);
    }

    @PutMapping("series/{id}")
    public ResponseEntity<?> updateSeries(@PathVariable Long id, @RequestBody CreateSeriesRequest seriesRequest) {
        Series updatedSeries;
        try {
            updatedSeries = seriesService.updateSeries(id, seriesRequest);
        } catch (SeriesNotFoundException e) {
            return ResponseEntity.badRequest().body("Series not found");
        }
        return ResponseEntity.ok().body(updatedSeries);
    }


}
