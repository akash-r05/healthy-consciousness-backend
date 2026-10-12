package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.SleepLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SleepLogRepository extends JpaRepository<SleepLog, Long> {

    @Query("SELECT s FROM SleepLog s WHERE s.user.id = :userId AND s.loggedAt >= :startDate AND s.loggedAt <= :endDate ORDER BY s.loggedAt DESC")
    Page<SleepLog> findByUserIdAndDateRange(@Param("userId") Long userId,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            Pageable pageable);
}
