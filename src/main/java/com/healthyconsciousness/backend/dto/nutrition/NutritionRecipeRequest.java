package com.healthyconsciousness.backend.dto.nutrition;

public record NutritionRecipeRequest(
        String title,
        String summary,
        String ingredients,
        String instructions,
        Integer prepTimeMinutes,
        Integer calories,
        Double proteinG,
        Double carbsG,
        Double fatG,
        String dietaryTag,
        boolean published
) {
}
