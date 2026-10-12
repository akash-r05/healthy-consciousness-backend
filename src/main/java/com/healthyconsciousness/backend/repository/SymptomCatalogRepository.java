package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.SymptomCatalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SymptomCatalogRepository extends JpaRepository<SymptomCatalog, Long> {

    @Query("SELECT s FROM SymptomCatalog s WHERE s.published = true ORDER BY s.createdAt DESC")
    Page<SymptomCatalog> findPublished(Pageable pageable);

    @Query("SELECT s FROM SymptomCatalog s WHERE s.published = true AND (:query IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.description) LIKE LOWER(CONCAT('%', :query, '%'))) ORDER BY s.createdAt DESC")
    Page<SymptomCatalog> searchSymptoms(@Param("query") String query, Pageable pageable);
}
