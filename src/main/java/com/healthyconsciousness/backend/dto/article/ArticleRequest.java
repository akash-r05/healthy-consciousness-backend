package com.healthyconsciousness.backend.dto.article;

public record ArticleRequest(
        String title,
        String summary,
        String content,
        String sourceName,
        String sourceUrl,
        String authorName,
        Long categoryId,
        boolean featured,
        boolean published,
        String slug
) {
}
