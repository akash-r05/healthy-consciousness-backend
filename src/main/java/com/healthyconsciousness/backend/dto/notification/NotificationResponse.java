package com.healthyconsciousness.backend.dto.notification;

public record NotificationResponse(
        Long id,
        String type,
        String title,
        String message,
        boolean read
) {
}
