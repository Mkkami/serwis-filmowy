package com.app.repository;

import com.app.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    @Query("""
            SELECT s FROM Series s
            """)
    public List<Series> getAll();
}
