package com.eWallet.notification.controller;

import com.eWallet.notification.dto.NotificationRequest;
import com.eWallet.notification.entity.Notification;
import com.eWallet.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notification Service is UP!");
    }

    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(
            @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(
                notificationService.createNotification(request));
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Notification>> getUserNotifications(
            @PathVariable String email) {
        return ResponseEntity.ok(
                notificationService.getUserNotifications(email));
    }

    @GetMapping("/unread/{email}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(
            @PathVariable String email) {
        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(email));
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }
}