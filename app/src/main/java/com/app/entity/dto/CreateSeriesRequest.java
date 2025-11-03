package com.app.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

import java.util.List;

public record CreateSeriesRequest(
        String title,
        Integer duration,
        Integer releaseYear,
        Integer endYear,
        List<String> categories
) {}
