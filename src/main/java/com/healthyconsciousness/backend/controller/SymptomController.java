package com.healthyconsciousness.backend.controller;

import com.healthyconsciousness.backend.dto.symptom.SymptomHistoryRequest;
import com.healthyconsciousness.backend.dto.symptom.SymptomHistoryResponse;
import com.healthyconsciousness.backend.dto.symptom.SymptomRequest;
import com.healthyconsciousness.backend.dto.symptom.SymptomResponse;
import com.healthyconsciousness.backend.service.SymptomService;
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
public class SymptomController {

    private final SymptomService symptomService;
    private final UserService userService;

    public SymptomController(SymptomService symptomService, UserService userService) {
        this.symptomService = symptomService;
        this.userService = userService;
    }

    @GetMapping("/symptoms")
    public ResponseEntity<Page<SymptomResponse>> listSymptoms(
            @RequestParam(required = false) String query,
            Pageable pageable) {
        return ResponseEntity.ok(symptomService.listSymptoms(query, pageable));
    }

    @GetMapping("/symptoms/{id}")
    public ResponseEntity<SymptomResponse> getSymptom(@PathVariable Long id) {
        return ResponseEntity.ok(symptomService.getSymptom(id));
    }

    @PostMapping("/admin/symptoms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SymptomResponse> createSymptom(@Valid @RequestBody SymptomRequest request) {
        return ResponseEntity.ok(symptomService.createSymptom(request));
    }

    @PutMapping("/admin/symptoms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SymptomResponse> updateSymptom(@PathVariable Long id,
                                                        @Valid @RequestBody SymptomRequest request) {
        return ResponseEntity.ok(symptomService.updateSymptom(id, request));
    }

    @DeleteMapping("/admin/symptoms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSymptom(@PathVariable Long id) {
        symptomService.deleteSymptom(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/symptoms/log")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SymptomHistoryResponse> logSymptom(@Valid @RequestBody SymptomHistoryRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Long userId = userService.findUserByEmail(email).getId();
        return ResponseEntity.ok(symptomService.logSymptom(userId, request));
    }

    @GetMapping("/symptoms/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<SymptomHistoryResponse>> getMySymptomHistory(Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Long userId = userService.findUserByEmail(email).getId();
        return ResponseEntity.ok(symptomService.getUserSymptomHistory(userId, pageable));
    }
}
