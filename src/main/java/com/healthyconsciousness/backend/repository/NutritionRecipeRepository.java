package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.NutritionRecipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionRecipeRepository extends JpaRepository<NutritionRecipe, Long> {

    @Query("SELECT r FROM NutritionRecipe r WHERE r.published = true ORDER BY r.createdAt DESC")
    Page<NutritionRecipe> findPublished(Pageable pageable);

    @Query("SELECT r FROM NutritionRecipe r WHERE r.published = true AND (:query IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(r.summary) LIKE LOWER(CONCAT('%', :query, '%'))) AND (:dietaryTag IS NULL OR r.dietaryTag = :dietaryTag) ORDER BY r.createdAt DESC")
    Page<NutritionRecipe> searchRecipes(@Param("query") String query,
                                       @Param("dietaryTag") String dietaryTag,
                                       Pageable pageable);
}
