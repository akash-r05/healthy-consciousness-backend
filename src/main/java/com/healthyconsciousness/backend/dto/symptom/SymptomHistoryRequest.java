package com.healthyconsciousness.backend.dto.symptom;

public record SymptomHistoryRequest(
        Long symptomId,
        String notes,
        String severity
) {
}
