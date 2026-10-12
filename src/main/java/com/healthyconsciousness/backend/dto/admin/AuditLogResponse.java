package com.healthyconsciousness.backend.dto.admin;

public record AuditLogResponse(
        Long id,
        String userEmail,
        String action,
        String entityName,
        String details,
        String createdAt
) {
}
