package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.HealthStatusResponse;
import com.healthyconsciousness.backend.entity.HealthCheck;
import com.healthyconsciousness.backend.repository.HealthCheckRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class HealthService {

    private final HealthCheckRepository healthCheckRepository;

    public HealthService(HealthCheckRepository healthCheckRepository) {
        this.healthCheckRepository = healthCheckRepository;
    }

    public HealthStatusResponse getHealthStatus() {
        healthCheckRepository.save(new HealthCheck("api-health-check"));

        return new HealthStatusResponse(
                "UP",
                "healthy-consciousness-backend",
                Instant.now().toString()
        );
    }
}
