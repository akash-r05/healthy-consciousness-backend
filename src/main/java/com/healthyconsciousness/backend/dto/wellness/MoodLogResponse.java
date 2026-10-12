package com.healthyconsciousness.backend.dto.wellness;

import java.time.LocalDateTime;

public record MoodLogResponse(
        Long id,
        String moodValue,
        String notes,
        LocalDateTime loggedAt
) {
}
