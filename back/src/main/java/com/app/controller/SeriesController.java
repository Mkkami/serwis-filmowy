package com.app.controller;

import com.app.entity.Series;
import com.app.entity.dto.series.CreateSeriesRequest;
import com.app.entity.dto.series.FullSeriesRequest;
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
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @Operation(summary = "Get all series")
    @ApiResponse(responseCode = "200", description = "Found the series",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Series.class))))
    @GetMapping("series")
    public ResponseEntity<List<Series>> getAllSeries() {
        return ResponseEntity.ok(seriesService.getAllSeries());
    }

    @Operation(summary = "Get a series by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the series",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullSeriesRequest.class))),
            @ApiResponse(responseCode = "400", description = "Series not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
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

    @Operation(summary = "Add a new series")
    @ApiResponse(responseCode = "201", description = "Series created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Series.class)))
    @PostMapping("series")
    public ResponseEntity<Series> addSeries(@RequestBody CreateSeriesRequest seriesRequest) {
        Series series = seriesService.createSeries(seriesRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(series);
    }

    @Operation(summary = "Update a series")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Series updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Series.class))),
            @ApiResponse(responseCode = "400", description = "Series not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
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

    @Operation(summary = "Delete a series")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Series deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Series not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("series/{id}")
    public ResponseEntity<?> deleteSeries(@PathVariable Long id) {
        try {
            seriesService.deleteSeries(id);
        } catch (SeriesNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


}
