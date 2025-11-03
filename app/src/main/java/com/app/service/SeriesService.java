package com.app.service;

import com.app.entity.Category;
import com.app.entity.Series;
import com.app.entity.dto.CreateSeriesRequest;
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

    public List<Series> getAllSeries() {
        return seriesRepository.getAll();
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
}