package com.healthyconsciousness.backend.dto.admin;

public record UserManagementRequest(
        String email,
        String firstName,
        String lastName,
        String role,
        boolean active
) {
}
