package com.healthyconsciousness.backend.dto.medication;

import java.time.LocalDateTime;

public record MedicationRecordResponse(
        Long id,
        String name,
        String dosage,
        String form,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String notes,
        LocalDateTime createdAt
) {
}
