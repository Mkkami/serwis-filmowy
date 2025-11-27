package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.OptionalDouble;

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

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Episode> episodes;

    @OneToMany(cascade = CascadeType.ALL) //add orphan removal for easier removal
    @JoinTable(
            name = "series_review_link",
            joinColumns = @JoinColumn(name = "series_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id")
    )
    private List<Review> reviews;

    private Double averageRating = 0.0;
    private Integer reviewCount = 0;

    public Float averageRating() {
        return ((float) reviews.stream().mapToInt(Review::getRating).average().orElse(Double.NaN));
    }

    public Integer countRatings() {
        return reviews.size();
    }

    public void addEpisode(Episode episode) {
        episodes.add(episode);
    }

    public boolean hasEpisodeNumber(Integer number) {
        return episodes.stream().anyMatch(ep -> ep.getEpisodeNumber() == number);
    }

    public void addReview(Review review) {
        reviews.add(review);
        recalculareReviews();
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        recalculareReviews();
    }

    private void recalculareReviews() {
        if (reviews.isEmpty()) {
            averageRating = 0.0;
            reviewCount = 0;
        } else {
            OptionalDouble avg = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average();

            averageRating = avg.orElse(0.0);
            reviewCount = reviews.size();
        }
    }
}
