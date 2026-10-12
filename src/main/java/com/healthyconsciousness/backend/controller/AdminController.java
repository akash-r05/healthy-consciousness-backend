package com.healthyconsciousness.backend.controller;

import com.healthyconsciousness.backend.dto.admin.AuditLogResponse;
import com.healthyconsciousness.backend.dto.admin.UserManagementResponse;
import com.healthyconsciousness.backend.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserManagementResponse>> listUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.listUsers(pageable));
    }

    @PutMapping("/admin/users/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserManagementResponse> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deactivateUser(id));
    }

    @PutMapping("/admin/users/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserManagementResponse> activateUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.activateUser(id));
    }

    @GetMapping("/admin/audit-logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AuditLogResponse>> getAuditLogs(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAuditLogs(pageable));
    }
}
