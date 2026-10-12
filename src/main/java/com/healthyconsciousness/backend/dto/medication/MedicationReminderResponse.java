package com.healthyconsciousness.backend.dto.medication;

public record MedicationReminderResponse(
        Long id,
        Long medicationId,
        String reminderTime,
        String frequency,
        String status
) {
}
