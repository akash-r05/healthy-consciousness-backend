package com.healthyconsciousness.backend.dto.dashboard;

import java.util.List;

public record DashboardSummary(
        String userName,
        Long totalSymptomLogs,
        Long totalWellnessGoals,
        Long activeMedications,
        Integer recentWaterIntakeMl,
        Double averageSleepHours,
        Integer recentStressLevel,
        List<String> upcomingReminders,
        Long unreadNotifications,
        List<String> recentArticles
) {
}
