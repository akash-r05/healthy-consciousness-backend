package com.healthyconsciousness.backend.dto.preventive;

import java.time.LocalDateTime;

public record PreventiveCheckupRequest(
        String title,
        LocalDateTime scheduledDate,
        String notes
) {
}
