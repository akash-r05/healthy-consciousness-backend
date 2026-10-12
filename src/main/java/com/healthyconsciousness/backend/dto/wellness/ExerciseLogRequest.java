package com.healthyconsciousness.backend.dto.wellness;

public record ExerciseLogRequest(
        String exerciseType,
        Integer durationMinutes,
        String intensity
) {
}
