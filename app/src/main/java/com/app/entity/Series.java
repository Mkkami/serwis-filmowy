package com.app.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private int releaseYear;
    private Integer endYear;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "series")
    private List<Episode> episodes;

}
