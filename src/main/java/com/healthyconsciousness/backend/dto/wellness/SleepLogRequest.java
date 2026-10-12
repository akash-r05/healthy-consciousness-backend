package com.healthyconsciousness.backend.dto.wellness;

public record SleepLogRequest(
        Double hoursSleep,
        String sleepQuality
) {
}
