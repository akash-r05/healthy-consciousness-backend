package com.healthyconsciousness.backend.dto.auth;

public record AuthResponse(
        String token,
        String tokenType,
        String email,
        String role,
        boolean active
) {
}
