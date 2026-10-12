package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.PreventiveCheckup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PreventiveCheckupRepository extends JpaRepository<PreventiveCheckup, Long> {

    @Query("SELECT c FROM PreventiveCheckup c WHERE c.user.id = :userId ORDER BY c.scheduledDate DESC")
    Page<PreventiveCheckup> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
