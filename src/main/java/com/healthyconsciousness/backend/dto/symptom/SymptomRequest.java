package com.healthyconsciousness.backend.dto.symptom;

public record SymptomRequest(
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
