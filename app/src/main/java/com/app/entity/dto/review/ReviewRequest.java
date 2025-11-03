package com.app.entity.dto.review;

public record ReviewRequest(
        Long id,
        Integer rating,
        String comment,
        String user
) {
}
