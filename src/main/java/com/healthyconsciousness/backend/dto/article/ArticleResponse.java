package com.healthyconsciousness.backend.dto.article;

public record ArticleResponse(
        Long id,
        Long categoryId,
        String categoryName,
        String title,
        String summary,
        String content,
        String sourceName,
        String sourceUrl,
        String authorName,
        boolean featured,
        boolean published,
        String slug
) {
}
