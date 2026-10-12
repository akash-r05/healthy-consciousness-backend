package com.healthyconsciousness.backend.dto.symptom;

public record SymptomResponse(
        Long id,
        String name,
        String description,
        String commonSigns,
        String whenToSeekHelp,
        String emergencyNote,
        String sourceName,
        String sourceUrl,
        boolean published
) {
}
