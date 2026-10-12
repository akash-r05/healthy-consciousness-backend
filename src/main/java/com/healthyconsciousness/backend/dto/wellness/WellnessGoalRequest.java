package com.healthyconsciousness.backend.dto.wellness;

import java.time.LocalDateTime;

public record WellnessGoalRequest(
        String title,
        String description,
        String metricType,
        Double targetValue,
        LocalDateTime targetDate
) {
}
