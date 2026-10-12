package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.WaterIntake;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface WaterIntakeRepository extends JpaRepository<WaterIntake, Long> {

    @Query("SELECT w FROM WaterIntake w WHERE w.user.id = :userId AND w.loggedAt >= :startDate AND w.loggedAt <= :endDate ORDER BY w.loggedAt DESC")
    Page<WaterIntake> findByUserIdAndDateRange(@Param("userId") Long userId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               Pageable pageable);
}
