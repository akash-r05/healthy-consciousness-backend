package com.healthyconsciousness.backend.dto.symptom;

import java.time.LocalDateTime;

public record SymptomHistoryResponse(
        Long id,
        Long userId,
        Long symptomId,
        String symptomName,
        String notes,
        String severity,
        LocalDateTime startedAt,
        LocalDateTime createdAt
) {
}
