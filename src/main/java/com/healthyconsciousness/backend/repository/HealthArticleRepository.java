package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.HealthArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthArticleRepository extends JpaRepository<HealthArticle, Long> {

    @Query("SELECT a FROM HealthArticle a WHERE a.published = true ORDER BY a.createdAt DESC")
    Page<HealthArticle> findPublished(Pageable pageable);

    @Query("SELECT a FROM HealthArticle a WHERE a.published = true AND (:categoryId IS NULL OR a.category.id = :categoryId) AND (:query IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(a.summary) LIKE LOWER(CONCAT('%', :query, '%'))) ORDER BY a.createdAt DESC")
    Page<HealthArticle> searchArticles(@Param("categoryId") Long categoryId,
                                      @Param("query") String query,
                                      Pageable pageable);
}
