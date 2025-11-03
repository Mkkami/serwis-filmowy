package com.app.entity.dto;

public record ReviewRequest(
        Long id,
        Integer rating,
        String comment,
        String user
) {
}
