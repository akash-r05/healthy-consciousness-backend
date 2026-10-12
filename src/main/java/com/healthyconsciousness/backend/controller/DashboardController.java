package com.healthyconsciousness.backend.controller;

import com.healthyconsciousness.backend.dto.dashboard.DashboardSummary;
import com.healthyconsciousness.backend.service.DashboardService;
import com.healthyconsciousness.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;

    public DashboardController(DashboardService dashboardService, UserService userService) {
        this.dashboardService = dashboardService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DashboardSummary> getDashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Long userId = userService.findUserByEmail(email).getId();
        return ResponseEntity.ok(dashboardService.getDashboardSummary(userId));
    }
}
