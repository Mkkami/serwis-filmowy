package com.app.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Film {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private int releaseYear;
    private int duration;

    @ManyToOne
    private Category category;

    @ManyToMany
    private Set<Actor> actors;



}
