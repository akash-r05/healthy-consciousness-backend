package com.healthyconsciousness.backend.dto.wellness;

import java.time.LocalDateTime;

public record ExerciseLogResponse(
        Long id,
        String exerciseType,
        Integer durationMinutes,
        String intensity,
        LocalDateTime loggedAt
) {
}
