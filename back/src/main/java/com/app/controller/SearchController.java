package com.app.controller;

import com.app.entity.dto.MediaRequest;
import com.app.entity.dto.SearchCriteria;
import com.app.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @Operation(summary = "Search for films and series")
    @ApiResponse(responseCode = "200", description = "Found media",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    @GetMapping("/search")
    public ResponseEntity<Page<MediaRequest>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false)List<Long> categoryIds,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
            ) {
        SearchCriteria criteria = new SearchCriteria(
                title,
                categoryIds,
                page,
                sortBy,
                sortDirection
        );

        Page<MediaRequest> resultPage = searchService.search(criteria);

        return ResponseEntity.ok(resultPage);
    }
}
