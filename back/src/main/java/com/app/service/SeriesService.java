package com.app.service;

import com.app.entity.Category;
import com.app.entity.Episode;
import com.app.entity.Review;
import com.app.entity.Series;
import com.app.entity.dto.DtoMapper;
import com.app.entity.dto.episode.NewEpisodeRequest;
import com.app.entity.dto.review.NewReviewRequest;
import com.app.entity.dto.series.CreateSeriesRequest;
import com.app.entity.dto.series.FullSeriesRequest;
import com.app.exception.EpisodeAlreadyExistsException;
import com.app.exception.EpisodeNotFoundException;
import com.app.exception.SeriesNotFoundException;
import com.app.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    public List<Series> getAllSeries() {
        return seriesRepository.getAll();
    }

    public FullSeriesRequest getSeries(Long id) {
        Series series = seriesRepository.findById(id).orElseThrow(() -> new SeriesNotFoundException(id));

        return DtoMapper.seriesToFullDto(series);

    }

    public Series createSeries(CreateSeriesRequest SeriesRequest) {
        Series series = new Series();
        series.setTitle(SeriesRequest.title());
//        series.setDuration(SeriesRequest.duration() > 0 ? SeriesRequest.duration() : 0);
        series.setReleaseYear(SeriesRequest.releaseYear() > 0 ? SeriesRequest.releaseYear() : 0);
        List<Category> categories = SeriesRequest.categories().stream()
                .map(cat -> categoryService.get(cat))
                .collect(Collectors.toList());

        series.setCategories(categories);
        seriesRepository.save(series);
        return series;
    }

    public Series updateSeries(Long id, CreateSeriesRequest seriesRequest) {
        Series series = seriesRepository.findById(id).orElseThrow( () -> new SeriesNotFoundException(id));
        if (seriesRequest.title() != null && !seriesRequest.title().isBlank()) {
            series.setTitle(seriesRequest.title());
        }
        if (seriesRequest.categories() != null && !seriesRequest.categories().isEmpty()) {
            series.setCategories( seriesRequest.categories().stream()
                    .map(cat -> categoryService.get(cat))
                    .collect(Collectors.toList()));
        }
        if (seriesRequest.endYear() != null && seriesRequest.endYear() > 0) {
            series.setEndYear(seriesRequest.endYear());
        }
        if (seriesRequest.releaseYear() != null && seriesRequest.releaseYear() > 0) {
            series.setReleaseYear(seriesRequest.releaseYear());
        }
        seriesRepository.save(series);
        return series;
    }


    public void addReview(Long id, NewReviewRequest reviewRequest, String name) {
        Series series = seriesRepository.findById(id).orElseThrow(() -> new SeriesNotFoundException(id));

        Review review = reviewService.addReviewToSeries(series, reviewRequest, name);

        series.addReview(review);
        seriesRepository.save(series);
    }

    public void deleteReview(Long seriesId, Long reviewId, String username) {
        Series series = seriesRepository.findById(seriesId).orElseThrow(() -> new SeriesNotFoundException(seriesId));
        
        // Find the review to remove
        Review reviewToRemove = series.getReviews().stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new com.app.exception.ReviewNotFoundException(reviewId));
        
        // This will check authorization and delete the review from database
        reviewService.deleteReview(reviewId, username);
        
        // Remove the review from the series' list and recalculate ratings
        series.removeReview(reviewToRemove);
        seriesRepository.save(series);
    }

    public List<Episode> getAllEpisodes(Long id) {
        Series series = seriesRepository.findById(id).orElseThrow(() -> new SeriesNotFoundException(id));

        return series.getEpisodes();
    }

    public void addEpisode(Long id, NewEpisodeRequest episodeRequest) {
        Series series = seriesRepository.findById(id).orElseThrow(() -> new SeriesNotFoundException(id));

        if (series.hasEpisodeNumber(episodeRequest.episodeNumber())) {
            throw new EpisodeAlreadyExistsException(episodeRequest.episodeNumber().toString());
        }

        Episode episode = new Episode();
        episode.setEpisodeNumber(episodeRequest.episodeNumber());
        episode.setSeries(series);
        episode.setReleaseDate(episodeRequest.releaseDate());
        episode.setTitle(episodeRequest.title());

        series.addEpisode(episode);

        seriesRepository.save(series);
    }

    public void deleteEpisode(Long seriesId, Long episodeId) {
        Series series = seriesRepository.findById(seriesId).orElseThrow(() -> new SeriesNotFoundException(seriesId));
        
        Episode episodeToRemove = series.getEpisodes().stream()
                .filter(episode -> episode.getId().equals(episodeId))
                .findFirst()
                .orElseThrow(() -> new EpisodeNotFoundException(episodeId));
        
        series.getEpisodes().remove(episodeToRemove);
        seriesRepository.save(series);
    }

    public void deleteSeries(Long id) {
        Series series = seriesRepository.findById(id).orElseThrow(() -> new SeriesNotFoundException(id));
        seriesRepository.delete(series);
    }
}