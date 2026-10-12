package com.healthyconsciousness.backend.dto.medication;

import java.time.LocalTime;

public record MedicationReminderRequest(
        Long medicationId,
        LocalTime reminderTime,
        String frequency
) {
}
