package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.SymptomHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SymptomHistoryRepository extends JpaRepository<SymptomHistory, Long> {

    @Query("SELECT sh FROM SymptomHistory sh WHERE sh.user.id = :userId ORDER BY sh.createdAt DESC")
    Page<SymptomHistory> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT sh FROM SymptomHistory sh WHERE sh.user.id = :userId AND sh.createdAt >= :startDate AND sh.createdAt <= :endDate ORDER BY sh.createdAt DESC")
    Page<SymptomHistory> findByUserIdAndDateRange(@Param("userId") Long userId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate,
                                                   Pageable pageable);
}
