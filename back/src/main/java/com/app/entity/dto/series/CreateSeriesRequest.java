package com.app.entity.dto.series;

import java.util.List;

public record CreateSeriesRequest(
        String title,
        Integer releaseYear,
        Integer endYear,
        List<String> categories
) {}
