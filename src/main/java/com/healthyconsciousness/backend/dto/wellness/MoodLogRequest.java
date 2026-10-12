package com.healthyconsciousness.backend.dto.wellness;

public record MoodLogRequest(
        String moodValue,
        String notes
) {
}
