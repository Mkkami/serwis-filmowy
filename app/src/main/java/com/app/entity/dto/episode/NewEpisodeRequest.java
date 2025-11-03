package com.app.entity.dto.episode;

import java.time.LocalDate;

public record NewEpisodeRequest(
        Integer episodeNumber,
        String title,
        LocalDate releaseDate
) {
}
