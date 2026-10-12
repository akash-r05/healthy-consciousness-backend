package com.healthyconsciousness.backend.controller;

import com.healthyconsciousness.backend.dto.HealthStatusResponse;
import com.healthyconsciousness.backend.service.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Health", description = "Application health and readiness checks")
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/health")
    @Operation(summary = "Returns application health status")
    public ResponseEntity<HealthStatusResponse> health() {
        return ResponseEntity.ok(healthService.getHealthStatus());
    }
}
