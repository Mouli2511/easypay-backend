package com.eWallet.notification.service;

import com.eWallet.notification.dto.NotificationRequest;
import com.eWallet.notification.entity.Notification;
import com.eWallet.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserEmail(request.getUserEmail());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(
                Notification.NotificationType.valueOf(request.getType().toUpperCase()));
        return notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(String userEmail) {
        return notificationRepository
                .findByUserEmailOrderByCreatedAtDesc(userEmail);
    }

    public List<Notification> getUnreadNotifications(String userEmail) {
        return notificationRepository
                .findByUserEmailAndIsReadFalse(userEmail);
    }

    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found!"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }
}