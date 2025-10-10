package com.app.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int seasonNumber;
    private int episodeNumber;
    private String title;
    private int duration;
    private LocalDate releaseDate;

    @ManyToOne
    private Series series;

}
