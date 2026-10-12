package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.admin.AuditLogResponse;
import com.healthyconsciousness.backend.dto.admin.UserManagementResponse;
import com.healthyconsciousness.backend.entity.AuditLog;
import com.healthyconsciousness.backend.entity.User;
import com.healthyconsciousness.backend.exception.ResourceNotFoundException;
import com.healthyconsciousness.backend.repository.AuditLogRepository;
import com.healthyconsciousness.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public AdminService(UserRepository userRepository, AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(readOnly = true)
    public Page<UserManagementResponse> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::toUserResponse);
    }

    @Transactional
    public UserManagementResponse deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        user.setActive(false);
        logAudit(null, "DEACTIVATE_USER", "User", "User ID: " + userId);
        return toUserResponse(userRepository.save(user));
    }

    @Transactional
    public UserManagementResponse activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        user.setActive(true);
        logAudit(null, "ACTIVATE_USER", "User", "User ID: " + userId);
        return toUserResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable)
                .map(this::toAuditResponse);
    }

    @Transactional
    public void logAudit(User user, String action, String entityName, String details) {
        AuditLog log = AuditLog.builder()
                .user(user)
                .action(action)
                .entityName(entityName)
                .details(details)
                .build();
        auditLogRepository.save(log);
    }

    private UserManagementResponse toUserResponse(User user) {
        return new UserManagementResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name(),
                user.isActive(),
                user.getCreatedAt().toString()
        );
    }

    private AuditLogResponse toAuditResponse(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getUser() != null ? log.getUser().getEmail() : "SYSTEM",
                log.getAction(),
                log.getEntityName(),
                log.getDetails(),
                log.getCreatedAt().toString()
        );
    }
}
