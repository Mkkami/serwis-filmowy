package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Film {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private int releaseYear;
    private int duration;

    @ManyToMany
    private List<Category> categories;

    @ManyToMany
    private List<Review> reviews;

    public Float averageRating() {
        return ((float) reviews.stream().mapToInt(Review::getRating).average().orElse(Double.NaN));
    }

    public Integer countRatings() {
        return reviews.size();
    }
}
