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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="film_review_link",
            joinColumns = @JoinColumn(name="film_id"),
            inverseJoinColumns = @JoinColumn(name="review_id")
    )
    private List<Review> reviews;

    private Double averageRating = 0.0;
    private Integer reviewCount = 0;

    public void addReview(Review review) {
        reviews.add(review);
        recalculateReviews();
    }

    public Double averageRating() {
        return ( reviews.stream().mapToInt(Review::getRating).average().orElse(Double.NaN));
    }

    public Integer countRatings() {
        return reviews.size();
    }

    public void recalculateReviews() {
        if (reviews.isEmpty()) {
            averageRating = 0.0;
            reviewCount = 0;
        } else {
            averageRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            reviewCount = reviews.size();
        }
    }
}
