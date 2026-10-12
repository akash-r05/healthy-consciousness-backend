package com.healthyconsciousness.backend.controller;

import com.healthyconsciousness.backend.dto.nutrition.NutritionRecipeRequest;
import com.healthyconsciousness.backend.dto.nutrition.NutritionRecipeResponse;
import com.healthyconsciousness.backend.service.NutritionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NutritionController {

    private final NutritionService nutritionService;

    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    @GetMapping("/recipes")
    public ResponseEntity<Page<NutritionRecipeResponse>> listRecipes(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String dietaryTag,
            Pageable pageable) {
        return ResponseEntity.ok(nutritionService.listRecipes(query, dietaryTag, pageable));
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<NutritionRecipeResponse> getRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(nutritionService.getRecipe(id));
    }

    @PostMapping("/admin/recipes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NutritionRecipeResponse> createRecipe(@RequestParam Long createdByUserId,
                                                               @Valid @RequestBody NutritionRecipeRequest request) {
        return ResponseEntity.ok(nutritionService.createRecipe(createdByUserId, request));
    }

    @PutMapping("/admin/recipes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NutritionRecipeResponse> updateRecipe(@PathVariable Long id,
                                                               @Valid @RequestBody NutritionRecipeRequest request) {
        return ResponseEntity.ok(nutritionService.updateRecipe(id, request));
    }

    @DeleteMapping("/admin/recipes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        nutritionService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
