package com.healthyconsciousness.backend.controller;

import com.healthyconsciousness.backend.dto.medication.MedicationRecordRequest;
import com.healthyconsciousness.backend.dto.medication.MedicationRecordResponse;
import com.healthyconsciousness.backend.dto.medication.MedicationReminderRequest;
import com.healthyconsciousness.backend.dto.medication.MedicationReminderResponse;
import com.healthyconsciousness.backend.service.MedicationService;
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
public class MedicationController {

    private final MedicationService medicationService;
    private final UserService userService;

    public MedicationController(MedicationService medicationService, UserService userService) {
        this.medicationService = medicationService;
        this.userService = userService;
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userService.findUserByEmail(email).getId();
    }

    @PostMapping("/medications")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MedicationRecordResponse> addMedication(@Valid @RequestBody MedicationRecordRequest request) {
        return ResponseEntity.ok(medicationService.addMedication(getCurrentUserId(), request));
    }

    @GetMapping("/medications")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<MedicationRecordResponse>> getActiveMedications(Pageable pageable) {
        return ResponseEntity.ok(medicationService.getActiveMedications(getCurrentUserId(), pageable));
    }

    @PostMapping("/medications/{id}/reminders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MedicationReminderResponse> setReminder(@PathVariable Long id,
                                                                 @Valid @RequestBody MedicationReminderRequest request) {
        MedicationReminderRequest adjustedRequest = new MedicationReminderRequest(id, request.reminderTime(), request.frequency());
        return ResponseEntity.ok(medicationService.setReminder(adjustedRequest));
    }

    @DeleteMapping("/medications/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeMedication(@PathVariable Long id) {
        medicationService.removeMedication(id, getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}
