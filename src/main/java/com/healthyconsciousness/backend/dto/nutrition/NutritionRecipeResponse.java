package com.healthyconsciousness.backend.dto.nutrition;

import java.time.LocalDateTime;

public record NutritionRecipeResponse(
        Long id,
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
        boolean published,
        LocalDateTime createdAt
) {
}
