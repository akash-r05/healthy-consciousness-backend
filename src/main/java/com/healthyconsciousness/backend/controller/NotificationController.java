package com.healthyconsciousness.backend.controller;

import com.healthyconsciousness.backend.dto.notification.NotificationResponse;
import com.healthyconsciousness.backend.service.NotificationService;
import com.healthyconsciousness.backend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userService.findUserByEmail(email).getId();
    }

    @GetMapping("/notifications")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationResponse>> getNotifications(Pageable pageable) {
        return ResponseEntity.ok(notificationService.getUserNotifications(getCurrentUserId(), pageable));
    }

    @GetMapping("/notifications/unread")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationResponse>> getUnreadNotifications(Pageable pageable) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(getCurrentUserId(), pageable));
    }

    @PutMapping("/notifications/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id, getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}
