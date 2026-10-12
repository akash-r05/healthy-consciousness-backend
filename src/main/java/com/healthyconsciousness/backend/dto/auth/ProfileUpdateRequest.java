package com.healthyconsciousness.backend.dto.auth;

public record ProfileUpdateRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String bio
) {
}
