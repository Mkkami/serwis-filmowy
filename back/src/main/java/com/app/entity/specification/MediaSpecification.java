package com.app.entity.specification;

import com.app.entity.Category;
import com.app.entity.Film;
import com.app.entity.dto.SearchCriteria;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MediaSpecification<T> implements Specification<T> {

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.titleSearchTerm() != null && !criteria.titleSearchTerm().isBlank()) {
            String likePattern = "%" + criteria.titleSearchTerm().toLowerCase() + "%";
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern));
        }

        if (criteria.categoryIds() != null && !criteria.categoryIds().isEmpty()) {
            Join<Film, Category> categoriesJoin = root.join("categories", JoinType.INNER);
            predicates.add(categoriesJoin.get("id").in(criteria.categoryIds()));
            query.distinct(true);
        }

        String sortBy = criteria.sortBy();
        String sortDirection = criteria.sortDirection();

        String entitySortField = switch (sortBy) {
            case "averageRating" -> "averageRating";
            case "releaseYear" -> "releaseYear";
            case "reviewCount" -> "reviewCount";
            default -> "title";
        };

        Order order = sortDirection.equalsIgnoreCase("desc")
                ? criteriaBuilder.desc(root.get(entitySortField))
                : criteriaBuilder.asc(root.get(entitySortField));
        query.orderBy(order);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
