package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private int releaseYear;
    private Integer endYear;

    @ManyToMany
    private List<Category> categories;

    @OneToMany(mappedBy = "series")
    private List<Episode> episodes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "series_review_link",
            joinColumns = @JoinColumn(name = "series_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id")
    )
    private List<Review> reviews;

    public void addReview(Review review) {
        reviews.add(review);
    }

    public Float averageRating() {
        return ((float) reviews.stream().mapToInt(Review::getRating).average().orElse(Double.NaN));
    }

    public Integer countRatings() {
        return reviews.size();
    }
}
