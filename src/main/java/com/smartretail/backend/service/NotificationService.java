package com.smartretail.backend.service;

import com.smartretail.backend.dto.notification.NotificationRequest;
import com.smartretail.backend.dto.notification.NotificationResponse;
import com.smartretail.backend.entity.Notification;
import com.smartretail.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getUserNotifications(Boolean isRead) {
        Long userId = getCurrentUserId(); // TODO: Get from authentication

        List<Notification> notifications;
        if (isRead == null) {
            notifications = notificationRepository.findByUserId(userId);
        } else if (isRead) {
            notifications = notificationRepository.findByUserIdAndIsReadTrue(userId);
        } else {
            notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        }

        return notifications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setLink(request.getLink());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        return toResponse(saved);
    }

    @Transactional
    public NotificationResponse markAsRead(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification không tồn tại"));
        notification.setIsRead(true);
        Notification saved = notificationRepository.save(notification);
        return toResponse(saved);
    }

    @Transactional
    public void markAllAsRead() {
        Long userId = getCurrentUserId();
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        notifications.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(notifications);
    }

    public void deleteNotification(Integer id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification không tồn tại"));
        notificationRepository.delete(notification);
    }

    private Long getCurrentUserId() {
        // TODO: Get from authentication
        return 1L; // Mock
    }

    private NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getNotificationId())
                .userId(notification.getUserId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .link(notification.getLink())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
