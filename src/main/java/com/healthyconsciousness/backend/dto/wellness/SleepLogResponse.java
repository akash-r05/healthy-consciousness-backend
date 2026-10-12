package com.healthyconsciousness.backend.dto.wellness;

import java.time.LocalDateTime;

public record SleepLogResponse(
        Long id,
        Double hoursSleep,
        String sleepQuality,
        LocalDateTime loggedAt
) {
}
