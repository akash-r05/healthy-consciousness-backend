package com.healthyconsciousness.backend.dto.wellness;

import java.time.LocalDateTime;

public record WellnessGoalResponse(
        Long id,
        String title,
        String description,
        String metricType,
        Double targetValue,
        Double currentValue,
        LocalDateTime targetDate,
        String status,
        LocalDateTime createdAt
) {
}
