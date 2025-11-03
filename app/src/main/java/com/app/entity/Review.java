package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer rating;
    private String comment;

    @ManyToOne
    private User user;
}
