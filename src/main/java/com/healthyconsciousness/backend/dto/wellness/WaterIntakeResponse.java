package com.healthyconsciousness.backend.dto.wellness;

import java.time.LocalDateTime;

public record WaterIntakeResponse(
        Long id,
        Integer amountMl,
        LocalDateTime loggedAt
) {
}
