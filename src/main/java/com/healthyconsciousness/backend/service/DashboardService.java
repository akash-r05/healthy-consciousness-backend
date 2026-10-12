package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.dashboard.DashboardSummary;
import com.healthyconsciousness.backend.entity.User;
import com.healthyconsciousness.backend.exception.ResourceNotFoundException;
import com.healthyconsciousness.backend.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final SymptomHistoryRepository symptomHistoryRepository;
    private final WellnessGoalRepository goalRepository;
    private final MedicationRecordRepository medicationRepository;
    private final WaterIntakeRepository waterIntakeRepository;
    private final SleepLogRepository sleepLogRepository;
    private final StressLogRepository stressLogRepository;
    private final NotificationRecordRepository notificationRepository;
    private final HealthArticleRepository articleRepository;
    private final PreventiveCheckupRepository checkupRepository;

    public DashboardService(UserRepository userRepository,
                           SymptomHistoryRepository symptomHistoryRepository,
                           WellnessGoalRepository goalRepository,
                           MedicationRecordRepository medicationRepository,
                           WaterIntakeRepository waterIntakeRepository,
                           SleepLogRepository sleepLogRepository,
                           StressLogRepository stressLogRepository,
                           NotificationRecordRepository notificationRepository,
                           HealthArticleRepository articleRepository,
                           PreventiveCheckupRepository checkupRepository) {
        this.userRepository = userRepository;
        this.symptomHistoryRepository = symptomHistoryRepository;
        this.goalRepository = goalRepository;
        this.medicationRepository = medicationRepository;
        this.waterIntakeRepository = waterIntakeRepository;
        this.sleepLogRepository = sleepLogRepository;
        this.stressLogRepository = stressLogRepository;
        this.notificationRepository = notificationRepository;
        this.articleRepository = articleRepository;
        this.checkupRepository = checkupRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummary getDashboardSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Pageable limit5 = PageRequest.of(0, 5);
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        LocalDateTime now = LocalDateTime.now();

        long totalSymptomLogs = symptomHistoryRepository.findByUserId(userId, limit5).getTotalElements();
        long totalWellnessGoals = goalRepository.findActiveGoalsByUserId(userId, limit5).getTotalElements();
        long activeMedications = medicationRepository.findActiveMedicationsByUserId(userId, limit5).getTotalElements();
        long unreadNotifications = notificationRepository.findUnreadByUserId(userId, limit5).getTotalElements();

        Integer recentWaterIntakeMl = 0;
        var waterPage = waterIntakeRepository.findByUserIdAndDateRange(userId, thirtyDaysAgo, now, PageRequest.of(0, 1));
        if (waterPage.hasContent()) {
            recentWaterIntakeMl = waterPage.getContent().get(0).getAmountMl();
        }

        Double averageSleepHours = 0.0;
        var sleepPage = sleepLogRepository.findByUserIdAndDateRange(userId, thirtyDaysAgo, now, PageRequest.of(0, 30));
        if (sleepPage.hasContent()) {
            averageSleepHours = sleepPage.getContent().stream()
                    .mapToDouble(s -> s.getHoursSleep())
                    .average()
                    .orElse(0.0);
        }

        Integer recentStressLevel = 0;
        var stressPage = stressLogRepository.findByUserIdAndDateRange(userId, thirtyDaysAgo, now, PageRequest.of(0, 1));
        if (stressPage.hasContent()) {
            recentStressLevel = stressPage.getContent().get(0).getStressLevel();
        }

        List<String> upcomingReminders = new ArrayList<>();
        // Placeholder for upcoming reminders
        upcomingReminders.add("Take medication at 08:00");

        List<String> recentArticles = new ArrayList<>();
        var articlePage = articleRepository.findPublished(PageRequest.of(0, 3));
        articlePage.getContent().forEach(a -> recentArticles.add(a.getTitle()));

        return new DashboardSummary(
                user.getFirstName() + " " + user.getLastName(),
                totalSymptomLogs,
                totalWellnessGoals,
                activeMedications,
                recentWaterIntakeMl,
                averageSleepHours,
                recentStressLevel,
                upcomingReminders,
                unreadNotifications,
                recentArticles
        );
    }
}
