package com.healthyconsciousness.backend.dto.auth;

public record PasswordChangeRequest(
        String currentPassword,
        String newPassword
) {
}
