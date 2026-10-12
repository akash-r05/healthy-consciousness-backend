package com.healthyconsciousness.backend.dto.wellness;

public record StressLogRequest(
        Integer stressLevel,
        String notes
) {
}
