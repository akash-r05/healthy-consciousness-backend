package com.healthyconsciousness.backend.service;

import com.healthyconsciousness.backend.dto.notification.NotificationResponse;
import com.healthyconsciousness.backend.entity.NotificationRecord;
import com.healthyconsciousness.backend.entity.User;
import com.healthyconsciousness.backend.exception.ResourceNotFoundException;
import com.healthyconsciousness.backend.repository.NotificationRecordRepository;
import com.healthyconsciousness.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private final NotificationRecordRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRecordRepository notificationRepository,
                              UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createNotification(Long userId, String type, String title, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        NotificationRecord notification = NotificationRecord.builder()
                .user(user)
                .type(type)
                .title(title)
                .message(message)
                .read(false)
                .build();
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUnreadNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findUnreadByUserId(userId, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        NotificationRecord notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + notificationId));
        if (!notification.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Unauthorized access to notification");
        }
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    private NotificationResponse toResponse(NotificationRecord notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isRead()
        );
    }
}
