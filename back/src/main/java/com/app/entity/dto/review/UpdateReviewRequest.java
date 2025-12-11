package com.app.entity.dto.review;

public record UpdateReviewRequest(
        Integer rating,
        String comment
) {
}
