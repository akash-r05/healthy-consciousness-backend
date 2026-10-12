package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.search.SearchResult;
import com.healthyconsciousness.backend.repository.HealthArticleRepository;
import com.healthyconsciousness.backend.repository.NutritionRecipeRepository;
import com.healthyconsciousness.backend.repository.SymptomCatalogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    private final HealthArticleRepository articleRepository;
    private final SymptomCatalogRepository symptomRepository;
    private final NutritionRecipeRepository recipeRepository;

    public SearchService(HealthArticleRepository articleRepository,
                        SymptomCatalogRepository symptomRepository,
                        NutritionRecipeRepository recipeRepository) {
        this.articleRepository = articleRepository;
        this.symptomRepository = symptomRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional(readOnly = true)
    public List<SearchResult> globalSearch(String query) {
        List<SearchResult> results = new ArrayList<>();
        Pageable limit = PageRequest.of(0, 10);

        // Search articles
        articleRepository.searchArticles(null, query, limit).forEach(article ->
            results.add(new SearchResult(
                "ARTICLE",
                article.getId(),
                article.getTitle(),
                article.getSummary(),
                article.getCategory() != null ? article.getCategory().getName() : null
            ))
        );

        // Search symptoms
        symptomRepository.searchSymptoms(query, limit).forEach(symptom ->
            results.add(new SearchResult(
                "SYMPTOM",
                symptom.getId(),
                symptom.getName(),
                symptom.getDescription(),
                null
            ))
        );

        // Search recipes
        recipeRepository.searchRecipes(query, null, limit).forEach(recipe ->
            results.add(new SearchResult(
                "RECIPE",
                recipe.getId(),
                recipe.getTitle(),
                recipe.getSummary(),
                recipe.getDietaryTag()
            ))
        );

        return results;
    }
}
