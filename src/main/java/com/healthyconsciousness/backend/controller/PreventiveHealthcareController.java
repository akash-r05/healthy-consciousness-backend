package com.healthyconsciousness.backend.controller;

import com.healthyconsciousness.backend.dto.preventive.PreventiveCheckupRequest;
import com.healthyconsciousness.backend.dto.preventive.PreventiveCheckupResponse;
import com.healthyconsciousness.backend.dto.preventive.VaccinationRecordRequest;
import com.healthyconsciousness.backend.dto.preventive.VaccinationRecordResponse;
import com.healthyconsciousness.backend.service.PreventiveHealthcareService;
import com.healthyconsciousness.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PreventiveHealthcareController {

    private final PreventiveHealthcareService preventiveService;
    private final UserService userService;

    public PreventiveHealthcareController(PreventiveHealthcareService preventiveService,
                                         UserService userService) {
        this.preventiveService = preventiveService;
        this.userService = userService;
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userService.findUserByEmail(email).getId();
    }

    @PostMapping("/preventive/checkups")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PreventiveCheckupResponse> scheduleCheckup(@Valid @RequestBody PreventiveCheckupRequest request) {
        return ResponseEntity.ok(preventiveService.scheduleCheckup(getCurrentUserId(), request));
    }

    @GetMapping("/preventive/checkups")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<PreventiveCheckupResponse>> getUserCheckups(Pageable pageable) {
        return ResponseEntity.ok(preventiveService.getUserCheckups(getCurrentUserId(), pageable));
    }

    @PutMapping("/preventive/checkups/{id}/complete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PreventiveCheckupResponse> completeCheckup(@PathVariable Long id) {
        return ResponseEntity.ok(preventiveService.completeCheckup(id, getCurrentUserId()));
    }

    @PostMapping("/preventive/vaccinations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VaccinationRecordResponse> recordVaccination(@Valid @RequestBody VaccinationRecordRequest request) {
        return ResponseEntity.ok(preventiveService.recordVaccination(getCurrentUserId(), request));
    }

    @GetMapping("/preventive/vaccinations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<VaccinationRecordResponse>> getUserVaccinations(Pageable pageable) {
        return ResponseEntity.ok(preventiveService.getUserVaccinations(getCurrentUserId(), pageable));
    }
}
