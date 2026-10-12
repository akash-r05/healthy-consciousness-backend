# healthy-consciousness-backend
Medical Wellness Desktop Application Backend - Java Spring Boot REST API

This repository contains the backend API for the Healthy Consciousness medical wellness application.

## Stack
- Java 21
- Spring Boot 3.3.x
- Maven
- MySQL 8
- Spring Data JPA
- Flyway migrations
- Spring Boot Actuator
- Swagger/OpenAPI

## Project structure
- `src/main/java/com/healthyconsciousness/backend` - application source code
- `src/main/resources` - Spring configuration and Flyway scripts
- `src/test/java` - automated tests

## Prerequisites
- Java 21 JDK
- Maven 3.9+
- Docker and Docker Compose (for local MySQL)
- MySQL 8 (optional if you run the database directly)

## Local database setup
1. Copy the example environment file and adjust the values if needed:
   ```bash
   cp .env.example .env
   ```
2. Start MySQL with Docker Compose:
   ```bash
   docker compose up -d mysql
   ```
3. Verify the database is running:
   ```bash
   docker compose ps
   ```

## Run the application
1. Set environment variables (or use `.env` with Docker Compose):
   ```bash
   export DB_HOST=localhost
   export DB_PORT=3306
   export DB_NAME=healthy_consciousness
   export DB_USERNAME=healthy_consciousness
   export DB_PASSWORD=healthy_consciousness
   ```
2. Start the application:
   ```bash
   mvn spring-boot:run
   ```

The API will be available at:
- `http://localhost:8080/api/health`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI docs: `http://localhost:8080/v3/api-docs`

## Actuator endpoints
Actuator health information is exposed under:
- `http://localhost:8080/actuator/health`

## Flyway
Database migration scripts live in:
- `src/main/resources/db/migration`

## Notes
- Database credentials are read from environment variables instead of being hardcoded into source control.
- The default local MySQL values are examples only and should be changed in your environment if needed.
