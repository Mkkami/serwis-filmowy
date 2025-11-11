package com.app.entity.dto;

import java.util.List;

public record SearchCriteria(
        String titleSearchTerm,
        List<Long> categoryIds,
        Integer page,
        String sortBy,
        String sortDirection
) {
    public SearchCriteria {
        if (page == null || page < 0) {
            page = 0;
        }
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "title";
        }
        if (sortDirection == null || (!sortDirection.equalsIgnoreCase("asc")) && !sortDirection.equalsIgnoreCase("desc")) {
            sortDirection = "asc";
        }
    }
}
