package com.healthyconsciousness.backend.dto.wellness;

import java.time.LocalDateTime;

public record StressLogResponse(
        Long id,
        Integer stressLevel,
        String notes,
        LocalDateTime loggedAt
) {
}
