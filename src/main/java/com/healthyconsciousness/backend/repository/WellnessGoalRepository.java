package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.WellnessGoal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WellnessGoalRepository extends JpaRepository<WellnessGoal, Long> {

    @Query("SELECT g FROM WellnessGoal g WHERE g.user.id = :userId AND g.status = 'ACTIVE' ORDER BY g.createdAt DESC")
    Page<WellnessGoal> findActiveGoalsByUserId(@Param("userId") Long userId, Pageable pageable);
}
