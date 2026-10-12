package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.wellness.*;
import com.healthyconsciousness.backend.entity.*;
import com.healthyconsciousness.backend.exception.ResourceNotFoundException;
import com.healthyconsciousness.backend.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WellnessService {

    private final WaterIntakeRepository waterIntakeRepository;
    private final SleepLogRepository sleepLogRepository;
    private final ExerciseLogRepository exerciseLogRepository;
    private final MoodLogRepository moodLogRepository;
    private final StressLogRepository stressLogRepository;
    private final WellnessGoalRepository wellnessGoalRepository;
    private final UserRepository userRepository;

    public WellnessService(WaterIntakeRepository waterIntakeRepository,
                          SleepLogRepository sleepLogRepository,
                          ExerciseLogRepository exerciseLogRepository,
                          MoodLogRepository moodLogRepository,
                          StressLogRepository stressLogRepository,
                          WellnessGoalRepository wellnessGoalRepository,
                          UserRepository userRepository) {
        this.waterIntakeRepository = waterIntakeRepository;
        this.sleepLogRepository = sleepLogRepository;
        this.exerciseLogRepository = exerciseLogRepository;
        this.moodLogRepository = moodLogRepository;
        this.stressLogRepository = stressLogRepository;
        this.wellnessGoalRepository = wellnessGoalRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WaterIntakeResponse logWater(Long userId, WaterIntakeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        WaterIntake intake = WaterIntake.builder()
                .user(user)
                .amountMl(request.amountMl())
                .loggedAt(LocalDateTime.now())
                .build();
        return toWaterResponse(waterIntakeRepository.save(intake));
    }

    @Transactional
    public SleepLogResponse logSleep(Long userId, SleepLogRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        SleepLog log = SleepLog.builder()
                .user(user)
                .hoursSleep(request.hoursSleep())
                .sleepQuality(request.sleepQuality())
                .loggedAt(LocalDateTime.now())
                .build();
        return toSleepResponse(sleepLogRepository.save(log));
    }

    @Transactional
    public ExerciseLogResponse logExercise(Long userId, ExerciseLogRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        ExerciseLog log = ExerciseLog.builder()
                .user(user)
                .exerciseType(request.exerciseType())
                .durationMinutes(request.durationMinutes())
                .intensity(request.intensity())
                .loggedAt(LocalDateTime.now())
                .build();
        return toExerciseResponse(exerciseLogRepository.save(log));
    }

    @Transactional
    public MoodLogResponse logMood(Long userId, MoodLogRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        MoodLog log = MoodLog.builder()
                .user(user)
                .moodValue(request.moodValue())
                .notes(request.notes())
                .loggedAt(LocalDateTime.now())
                .build();
        return toMoodResponse(moodLogRepository.save(log));
    }

    @Transactional
    public StressLogResponse logStress(Long userId, StressLogRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        StressLog log = StressLog.builder()
                .user(user)
                .stressLevel(request.stressLevel())
                .notes(request.notes())
                .loggedAt(LocalDateTime.now())
                .build();
        return toStressResponse(stressLogRepository.save(log));
    }

    @Transactional
    public WellnessGoalResponse createGoal(Long userId, WellnessGoalRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        WellnessGoal goal = WellnessGoal.builder()
                .user(user)
                .title(request.title())
                .description(request.description())
                .metricType(request.metricType())
                .targetValue(request.targetValue())
                .currentValue(0.0)
                .targetDate(request.targetDate())
                .status("ACTIVE")
                .build();
        return toGoalResponse(wellnessGoalRepository.save(goal));
    }

    @Transactional(readOnly = true)
    public Page<WaterIntakeResponse> getWaterHistory(Long userId, Pageable pageable) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();
        return waterIntakeRepository.findByUserIdAndDateRange(userId, startDate, endDate, pageable)
                .map(this::toWaterResponse);
    }

    @Transactional(readOnly = true)
    public Page<SleepLogResponse> getSleepHistory(Long userId, Pageable pageable) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();
        return sleepLogRepository.findByUserIdAndDateRange(userId, startDate, endDate, pageable)
                .map(this::toSleepResponse);
    }

    @Transactional(readOnly = true)
    public Page<ExerciseLogResponse> getExerciseHistory(Long userId, Pageable pageable) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();
        return exerciseLogRepository.findByUserIdAndDateRange(userId, startDate, endDate, pageable)
                .map(this::toExerciseResponse);
    }

    @Transactional(readOnly = true)
    public Page<MoodLogResponse> getMoodHistory(Long userId, Pageable pageable) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();
        return moodLogRepository.findByUserIdAndDateRange(userId, startDate, endDate, pageable)
                .map(this::toMoodResponse);
    }

    @Transactional(readOnly = true)
    public Page<StressLogResponse> getStressHistory(Long userId, Pageable pageable) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();
        return stressLogRepository.findByUserIdAndDateRange(userId, startDate, endDate, pageable)
                .map(this::toStressResponse);
    }

    @Transactional(readOnly = true)
    public Page<WellnessGoalResponse> getActiveGoals(Long userId, Pageable pageable) {
        return wellnessGoalRepository.findActiveGoalsByUserId(userId, pageable)
                .map(this::toGoalResponse);
    }

    @Transactional
    public WellnessGoalResponse updateGoal(Long goalId, Long userId, WellnessGoalRequest request) {
        WellnessGoal goal = wellnessGoalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found: " + goalId));
        if (!goal.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Unauthorized access to goal");
        }
        if (request.title() != null) goal.setTitle(request.title());
        if (request.description() != null) goal.setDescription(request.description());
        if (request.targetValue() != null) goal.setTargetValue(request.targetValue());
        if (request.targetDate() != null) goal.setTargetDate(request.targetDate());
        return toGoalResponse(wellnessGoalRepository.save(goal));
    }

    private WaterIntakeResponse toWaterResponse(WaterIntake intake) {
        return new WaterIntakeResponse(intake.getId(), intake.getAmountMl(), intake.getLoggedAt());
    }

    private SleepLogResponse toSleepResponse(SleepLog log) {
        return new SleepLogResponse(log.getId(), log.getHoursSleep(), log.getSleepQuality(), log.getLoggedAt());
    }

    private ExerciseLogResponse toExerciseResponse(ExerciseLog log) {
        return new ExerciseLogResponse(log.getId(), log.getExerciseType(), log.getDurationMinutes(), log.getIntensity(), log.getLoggedAt());
    }

    private MoodLogResponse toMoodResponse(MoodLog log) {
        return new MoodLogResponse(log.getId(), log.getMoodValue(), log.getNotes(), log.getLoggedAt());
    }

    private StressLogResponse toStressResponse(StressLog log) {
        return new StressLogResponse(log.getId(), log.getStressLevel(), log.getNotes(), log.getLoggedAt());
    }

    private WellnessGoalResponse toGoalResponse(WellnessGoal goal) {
        return new WellnessGoalResponse(goal.getId(), goal.getTitle(), goal.getDescription(), goal.getMetricType(), goal.getTargetValue(), goal.getCurrentValue(), goal.getTargetDate(), goal.getStatus(), goal.getCreatedAt());
    }
}
