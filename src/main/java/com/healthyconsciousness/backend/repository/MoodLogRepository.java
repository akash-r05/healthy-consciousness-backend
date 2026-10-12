package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.MoodLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MoodLogRepository extends JpaRepository<MoodLog, Long> {

    @Query("SELECT m FROM MoodLog m WHERE m.user.id = :userId AND m.loggedAt >= :startDate AND m.loggedAt <= :endDate ORDER BY m.loggedAt DESC")
    Page<MoodLog> findByUserIdAndDateRange(@Param("userId") Long userId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           Pageable pageable);
}
