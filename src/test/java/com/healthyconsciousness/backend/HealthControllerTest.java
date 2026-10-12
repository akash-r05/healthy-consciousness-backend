package com.healthyconsciousness.backend;

import com.healthyconsciousness.backend.controller.HealthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.healthyconsciousness.backend.service.HealthService healthService;

    @Test
    void shouldReturnHealthStatus() throws Exception {
        when(healthService.getHealthStatus())
                .thenReturn(new com.healthyconsciousness.backend.dto.HealthStatusResponse("UP", "healthy-consciousness-backend", "2026-10-12T00:00:00Z"));

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("healthy-consciousness-backend"));
    }
}
