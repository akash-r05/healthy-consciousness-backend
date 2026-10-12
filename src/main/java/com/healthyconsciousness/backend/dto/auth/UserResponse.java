package com.healthyconsciousness.backend.dto.auth;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String bio,
        String role,
        boolean active
) {
}
