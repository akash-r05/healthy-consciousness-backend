package com.healthyconsciousness.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Healthy Consciousness Backend API",
                version = "1.0.0",
                description = "REST API for the Healthy Consciousness medical wellness application.",
                contact = @Contact(
                        name = "Healthy Consciousness Team",
                        email = "support@example.com"
                )
        )
)
public class OpenApiConfig {
}
