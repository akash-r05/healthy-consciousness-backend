package com.healthyconsciousness.backend.dto.admin;

public record UserManagementResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String role,
        boolean active,
        String createdAt
) {
}
