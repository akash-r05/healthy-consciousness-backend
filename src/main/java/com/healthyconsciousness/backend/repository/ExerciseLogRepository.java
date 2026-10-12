package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.ExerciseLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {

    @Query("SELECT e FROM ExerciseLog e WHERE e.user.id = :userId AND e.loggedAt >= :startDate AND e.loggedAt <= :endDate ORDER BY e.loggedAt DESC")
    Page<ExerciseLog> findByUserIdAndDateRange(@Param("userId") Long userId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               Pageable pageable);
}
