package com.healthyconsciousness.backend.dto.preventive;

import java.time.LocalDateTime;

public record VaccinationRecordResponse(
        Long id,
        String vaccineName,
        LocalDateTime administeredDate,
        LocalDateTime nextDueDate,
        String notes
) {
}
