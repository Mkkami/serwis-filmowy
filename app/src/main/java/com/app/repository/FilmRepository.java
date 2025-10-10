package com.app.repository;

import com.app.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("""
            SELECT f FROM Film f
            """)
    public List<Film> getAll();

}
