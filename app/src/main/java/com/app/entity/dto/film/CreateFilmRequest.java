package com.app.entity.dto.film;

import java.util.List;

public record CreateFilmRequest(
    String title,
    Integer duration,
    Integer releaseYear,
    List<String> categories
) {}
