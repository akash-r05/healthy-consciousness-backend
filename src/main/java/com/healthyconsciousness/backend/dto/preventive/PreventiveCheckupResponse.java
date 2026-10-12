package com.healthyconsciousness.backend.dto.preventive;

import java.time.LocalDateTime;

public record PreventiveCheckupResponse(
        Long id,
        String title,
        LocalDateTime scheduledDate,
        LocalDateTime completedDate,
        String status,
        String notes
) {
}
