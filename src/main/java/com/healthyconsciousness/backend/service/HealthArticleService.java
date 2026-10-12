package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.article.ArticleRequest;
import com.healthyconsciousness.backend.dto.article.ArticleResponse;
import com.healthyconsciousness.backend.entity.ArticleCategory;
import com.healthyconsciousness.backend.entity.HealthArticle;
import com.healthyconsciousness.backend.entity.User;
import com.healthyconsciousness.backend.exception.ApiException;
import com.healthyconsciousness.backend.exception.ResourceNotFoundException;
import com.healthyconsciousness.backend.repository.ArticleCategoryRepository;
import com.healthyconsciousness.backend.repository.HealthArticleRepository;
import com.healthyconsciousness.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class HealthArticleService {

    private final HealthArticleRepository articleRepository;
    private final ArticleCategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public HealthArticleService(HealthArticleRepository articleRepository,
                               ArticleCategoryRepository categoryRepository,
                               UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<ArticleResponse> listArticles(Long categoryId, String query, Pageable pageable) {
        return articleRepository.searchArticles(categoryId, query, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ArticleResponse getArticle(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public ArticleResponse createArticle(Long createdByUserId, ArticleRequest request) {
        User createdBy = userRepository.findById(createdByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + createdByUserId));

        ArticleCategory category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.categoryId()));

        String slug = request.slug() == null || request.slug().isBlank()
                ? request.title().trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-")
                : request.slug();

        HealthArticle article = HealthArticle.builder()
                .title(request.title().trim())
                .summary(request.summary())
                .content(request.content())
                .sourceName(request.sourceName())
                .sourceUrl(request.sourceUrl())
                .authorName(request.authorName())
                .category(category)
                .createdBy(createdBy)
                .featured(request.featured())
                .published(request.published())
                .slug(slug)
                .build();

        return toResponse(articleRepository.save(article));
    }

    @Transactional
    public ArticleResponse updateArticle(Long articleId, ArticleRequest request) {
        HealthArticle article = findById(articleId);
        if (request.categoryId() != null) {
            ArticleCategory category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.categoryId()));
            article.setCategory(category);
        }
        if (request.title() != null) article.setTitle(request.title().trim());
        if (request.summary() != null) article.setSummary(request.summary());
        if (request.content() != null) article.setContent(request.content());
        if (request.sourceName() != null) article.setSourceName(request.sourceName());
        if (request.sourceUrl() != null) article.setSourceUrl(request.sourceUrl());
        if (request.authorName() != null) article.setAuthorName(request.authorName());
        if (request.slug() != null && !request.slug().isBlank()) article.setSlug(request.slug());
        article.setFeatured(request.featured());
        article.setPublished(request.published());
        return toResponse(articleRepository.save(article));
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        HealthArticle article = findById(articleId);
        articleRepository.delete(article);
    }

    private HealthArticle findById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found: " + articleId));
    }

    private ArticleResponse toResponse(HealthArticle article) {
        return new ArticleResponse(
                article.getId(),
                article.getCategory() != null ? article.getCategory().getId() : null,
                article.getCategory() != null ? article.getCategory().getName() : null,
                article.getTitle(),
                article.getSummary(),
                article.getContent(),
                article.getSourceName(),
                article.getSourceUrl(),
                article.getAuthorName(),
                article.isFeatured(),
                article.isPublished(),
                article.getSlug()
        );
    }
}
