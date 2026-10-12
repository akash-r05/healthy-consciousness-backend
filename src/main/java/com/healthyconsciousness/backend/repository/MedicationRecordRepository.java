package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.MedicationRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRecordRepository extends JpaRepository<MedicationRecord, Long> {

    @Query("SELECT m FROM MedicationRecord m WHERE m.user.id = :userId AND (m.endDate IS NULL OR m.endDate > CURRENT_TIMESTAMP) ORDER BY m.createdAt DESC")
    Page<MedicationRecord> findActiveMedicationsByUserId(@Param("userId") Long userId, Pageable pageable);
}
