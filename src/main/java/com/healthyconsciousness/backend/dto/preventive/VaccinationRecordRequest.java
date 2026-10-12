package com.healthyconsciousness.backend.dto.preventive;

import java.time.LocalDateTime;

public record VaccinationRecordRequest(
        String vaccineName,
        LocalDateTime administeredDate,
        LocalDateTime nextDueDate,
        String notes
) {
}
