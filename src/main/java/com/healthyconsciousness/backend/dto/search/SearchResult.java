package com.healthyconsciousness.backend.dto.search;

public record SearchResult(
        String resourceType,
        Long resourceId,
        String title,
        String description,
        String category
) {
}
