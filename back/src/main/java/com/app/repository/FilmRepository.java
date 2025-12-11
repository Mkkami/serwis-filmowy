package com.app.repository;

import com.app.entity.Film;
import com.app.entity.dto.film.FilmRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film> {

    @Query("""
            SELECT f FROM Film f
            """)
    public List<Film> getAll();

    @Query("""
            SELECT new com.app.entity.dto.film.FilmRequest(
                f.id, f.title, f.releaseYear, f.averageRating, f.reviewCount
            )
            FROM Film f
            LEFT JOIN f.categories c
            WHERE (:title IS NULL OR LOWER(f.title) LIKE LOWER(CONCAT('%', :title, '%')))
            AND (:categories IS NULL OR c.id IN :categories)
            GROUP BY f.id, f.title, f.releaseYear, f.averageRating, f.reviewCount
            """)
    Page<FilmRequest> searchFilmsWithStats(
            @Param("title") String title,
            @Param("categories") List<Long> categories,
            Pageable pageable);

}