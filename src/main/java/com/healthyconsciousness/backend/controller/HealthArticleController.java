package com.healthyconsciousness.backend.controller;

import com.healthyconsciousness.backend.dto.article.ArticleRequest;
import com.healthyconsciousness.backend.dto.article.ArticleResponse;
import com.healthyconsciousness.backend.service.HealthArticleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HealthArticleController {

    private final HealthArticleService articleService;

    public HealthArticleController(HealthArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/articles")
    public ResponseEntity<Page<ArticleResponse>> listArticles(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String query,
            Pageable pageable) {
        return ResponseEntity.ok(articleService.listArticles(categoryId, query, pageable));
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getArticle(id));
    }

    @PostMapping("/admin/articles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArticleResponse> createArticle(@RequestParam Long createdByUserId,
                                                       @Valid @RequestBody ArticleRequest request) {
        return ResponseEntity.ok(articleService.createArticle(createdByUserId, request));
    }

    @PutMapping("/admin/articles/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable Long id,
                                                       @Valid @RequestBody ArticleRequest request) {
        return ResponseEntity.ok(articleService.updateArticle(id, request));
    }

    @DeleteMapping("/admin/articles/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
