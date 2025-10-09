package com.app.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int value;
    private String comment;
    private LocalDate date;

    @ManyToOne
    private User user;

    @ManyToOne
    private Film film;

    @ManyToOne
    private Episode episode;
}
