package com.app.entity.dto.review;

public record NewReviewRequest(
        Integer rating,
        String comment
) {
}
