package com.app.repository;

import com.app.entity.Film;
import com.app.entity.dto.film.FilmRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("""
            SELECT f FROM Film f
            """)
    public List<Film> getAll();


}