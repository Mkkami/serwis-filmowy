package com.app.service;

import com.app.entity.Film;
import com.app.entity.Series;
import com.app.entity.dto.MediaRequest;
import com.app.entity.dto.SearchCriteria;
import com.app.entity.specification.MediaSpecification;
import com.app.repository.FilmRepository;
import com.app.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final FilmRepository filmRepository;
    private final SeriesRepository seriesRepository;
    private static final int PAGE_SIZE = 1;

    public Page<MediaRequest> search(SearchCriteria criteria) {
        Specification<Film> filmSpec = new MediaSpecification<>(criteria);
        Specification<Series> seriesSpec = new MediaSpecification<>(criteria);

        List<Film> films = filmRepository.findAll(filmSpec);
        List<Series> seriesList = seriesRepository.findAll(seriesSpec);

        Stream<MediaRequest> filmRequests = films.stream().map(MediaRequest::fromFilm);
        Stream<MediaRequest> seriesRequests = seriesList.stream().map(MediaRequest::fromSeries);

        List<MediaRequest> allMedia = Stream.concat(filmRequests, seriesRequests).toList();

        Comparator<MediaRequest> comparator = getMediaComparator(criteria.sortBy(), criteria.sortDirection());

        List<MediaRequest> sortedMedia = allMedia.stream().sorted(comparator).toList();

        int totalElements = sortedMedia.size();
        int fromIndex = criteria.page() * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalElements);

        List<MediaRequest> pageContent = (fromIndex < totalElements)
                ? sortedMedia.subList(fromIndex, toIndex)
                : List.of();

        return new PageImpl<>(
                pageContent,
                PageRequest.of(criteria.page(), PAGE_SIZE, Sort.unsorted()),
                totalElements
        );
    }

    private Comparator<MediaRequest> getMediaComparator(String sortBy, String sortDirection) {
        Comparator<MediaRequest> comparator;

        comparator = switch (sortBy) {
            case "averageRating" -> Comparator.comparing(MediaRequest::averageRating, Comparator.nullsLast(Double::compareTo));
            case "releaseYear" -> Comparator.comparing(MediaRequest::releaseYear, Comparator.nullsLast(Integer::compareTo));
            case "reviewCount" -> Comparator.comparing(MediaRequest::reviewCount, Comparator.nullsLast(Integer::compareTo));
            default -> Comparator.comparing(MediaRequest::title, Comparator.nullsLast(String::compareTo));
        };

        if (sortDirection.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}
