package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.VaccinationRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {

    @Query("SELECT v FROM VaccinationRecord v WHERE v.user.id = :userId ORDER BY v.administeredDate DESC")
    Page<VaccinationRecord> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
