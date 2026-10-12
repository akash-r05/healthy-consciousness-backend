package com.healthyconsciousness.backend.repository;

import com.healthyconsciousness.backend.entity.NotificationRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRecordRepository extends JpaRepository<NotificationRecord, Long> {

    @Query("SELECT n FROM NotificationRecord n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    Page<NotificationRecord> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT n FROM NotificationRecord n WHERE n.user.id = :userId AND n.read = false ORDER BY n.createdAt DESC")
    Page<NotificationRecord> findUnreadByUserId(@Param("userId") Long userId, Pageable pageable);
}
