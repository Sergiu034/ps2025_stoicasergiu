package com.example.demo.controller;

import com.example.demo.dto.notificationdto.NotificationDTO;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Long> sendNotification(@RequestBody NotificationDTO dto) {
        Long id = notificationService.sendNotification(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/unread/{userEmail}")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable String userEmail) {
        List<NotificationDTO> notifications = notificationService.getUnreadNotifications(userEmail);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/mark-as-read/{userEmail}")
    public ResponseEntity<Void> markAllAsRead(@PathVariable String userEmail) {
        notificationService.markAllAsRead(userEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        List<NotificationDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }
}
