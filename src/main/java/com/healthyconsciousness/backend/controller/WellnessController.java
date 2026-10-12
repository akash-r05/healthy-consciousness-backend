package com.healthyconsciousness.backend.controller;

import com.healthyconsciousness.backend.dto.wellness.*;
import com.healthyconsciousness.backend.service.UserService;
import com.healthyconsciousness.backend.service.WellnessService;
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
public class WellnessController {

    private final WellnessService wellnessService;
    private final UserService userService;

    public WellnessController(WellnessService wellnessService, UserService userService) {
        this.wellnessService = wellnessService;
        this.userService = userService;
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userService.findUserByEmail(email).getId();
    }

    @PostMapping("/wellness/water")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WaterIntakeResponse> logWater(@Valid @RequestBody WaterIntakeRequest request) {
        return ResponseEntity.ok(wellnessService.logWater(getCurrentUserId(), request));
    }

    @GetMapping("/wellness/water")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<WaterIntakeResponse>> getWaterHistory(Pageable pageable) {
        return ResponseEntity.ok(wellnessService.getWaterHistory(getCurrentUserId(), pageable));
    }

    @PostMapping("/wellness/sleep")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SleepLogResponse> logSleep(@Valid @RequestBody SleepLogRequest request) {
        return ResponseEntity.ok(wellnessService.logSleep(getCurrentUserId(), request));
    }

    @GetMapping("/wellness/sleep")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<SleepLogResponse>> getSleepHistory(Pageable pageable) {
        return ResponseEntity.ok(wellnessService.getSleepHistory(getCurrentUserId(), pageable));
    }

    @PostMapping("/wellness/exercise")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ExerciseLogResponse> logExercise(@Valid @RequestBody ExerciseLogRequest request) {
        return ResponseEntity.ok(wellnessService.logExercise(getCurrentUserId(), request));
    }

    @GetMapping("/wellness/exercise")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ExerciseLogResponse>> getExerciseHistory(Pageable pageable) {
        return ResponseEntity.ok(wellnessService.getExerciseHistory(getCurrentUserId(), pageable));
    }

    @PostMapping("/wellness/mood")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MoodLogResponse> logMood(@Valid @RequestBody MoodLogRequest request) {
        return ResponseEntity.ok(wellnessService.logMood(getCurrentUserId(), request));
    }

    @GetMapping("/wellness/mood")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<MoodLogResponse>> getMoodHistory(Pageable pageable) {
        return ResponseEntity.ok(wellnessService.getMoodHistory(getCurrentUserId(), pageable));
    }

    @PostMapping("/wellness/stress")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StressLogResponse> logStress(@Valid @RequestBody StressLogRequest request) {
        return ResponseEntity.ok(wellnessService.logStress(getCurrentUserId(), request));
    }

    @GetMapping("/wellness/stress")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<StressLogResponse>> getStressHistory(Pageable pageable) {
        return ResponseEntity.ok(wellnessService.getStressHistory(getCurrentUserId(), pageable));
    }

    @PostMapping("/wellness/goals")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WellnessGoalResponse> createGoal(@Valid @RequestBody WellnessGoalRequest request) {
        return ResponseEntity.ok(wellnessService.createGoal(getCurrentUserId(), request));
    }

    @GetMapping("/wellness/goals")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<WellnessGoalResponse>> getActiveGoals(Pageable pageable) {
        return ResponseEntity.ok(wellnessService.getActiveGoals(getCurrentUserId(), pageable));
    }

    @PutMapping("/wellness/goals/{goalId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WellnessGoalResponse> updateGoal(@PathVariable Long goalId,
                                                          @Valid @RequestBody WellnessGoalRequest request) {
        return ResponseEntity.ok(wellnessService.updateGoal(goalId, getCurrentUserId(), request));
    }
}
