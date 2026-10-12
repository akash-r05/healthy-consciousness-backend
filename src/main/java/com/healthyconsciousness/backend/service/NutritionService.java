package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.nutrition.NutritionRecipeRequest;
import com.healthyconsciousness.backend.dto.nutrition.NutritionRecipeResponse;
import com.healthyconsciousness.backend.entity.NutritionRecipe;
import com.healthyconsciousness.backend.entity.User;
import com.healthyconsciousness.backend.exception.ResourceNotFoundException;
import com.healthyconsciousness.backend.repository.NutritionRecipeRepository;
import com.healthyconsciousness.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NutritionService {

    private final NutritionRecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public NutritionService(NutritionRecipeRepository recipeRepository,
                           UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<NutritionRecipeResponse> listRecipes(String query, String dietaryTag, Pageable pageable) {
        return recipeRepository.searchRecipes(query, dietaryTag, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public NutritionRecipeResponse getRecipe(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public NutritionRecipeResponse createRecipe(Long createdByUserId, NutritionRecipeRequest request) {
        User createdBy = userRepository.findById(createdByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + createdByUserId));
        NutritionRecipe recipe = NutritionRecipe.builder()
                .title(request.title().trim())
                .summary(request.summary())
                .ingredients(request.ingredients())
                .instructions(request.instructions())
                .prepTimeMinutes(request.prepTimeMinutes())
                .calories(request.calories())
                .proteinG(request.proteinG())
                .carbsG(request.carbsG())
                .fatG(request.fatG())
                .dietaryTag(request.dietaryTag())
                .published(request.published())
                .createdBy(createdBy)
                .build();
        return toResponse(recipeRepository.save(recipe));
    }

    @Transactional
    public NutritionRecipeResponse updateRecipe(Long recipeId, NutritionRecipeRequest request) {
        NutritionRecipe recipe = findById(recipeId);
        if (request.title() != null) recipe.setTitle(request.title().trim());
        if (request.summary() != null) recipe.setSummary(request.summary());
        if (request.ingredients() != null) recipe.setIngredients(request.ingredients());
        if (request.instructions() != null) recipe.setInstructions(request.instructions());
        if (request.prepTimeMinutes() != null) recipe.setPrepTimeMinutes(request.prepTimeMinutes());
        if (request.calories() != null) recipe.setCalories(request.calories());
        if (request.proteinG() != null) recipe.setProteinG(request.proteinG());
        if (request.carbsG() != null) recipe.setCarbsG(request.carbsG());
        if (request.fatG() != null) recipe.setFatG(request.fatG());
        if (request.dietaryTag() != null) recipe.setDietaryTag(request.dietaryTag());
        recipe.setPublished(request.published());
        return toResponse(recipeRepository.save(recipe));
    }

    @Transactional
    public void deleteRecipe(Long recipeId) {
        NutritionRecipe recipe = findById(recipeId);
        recipeRepository.delete(recipe);
    }

    private NutritionRecipe findById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found: " + id));
    }

    private NutritionRecipeResponse toResponse(NutritionRecipe recipe) {
        return new NutritionRecipeResponse(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getSummary(),
                recipe.getIngredients(),
                recipe.getInstructions(),
                recipe.getPrepTimeMinutes(),
                recipe.getCalories(),
                recipe.getProteinG(),
                recipe.getCarbsG(),
                recipe.getFatG(),
                recipe.getDietaryTag(),
                recipe.isPublished(),
                recipe.getCreatedAt()
        );
    }
}
