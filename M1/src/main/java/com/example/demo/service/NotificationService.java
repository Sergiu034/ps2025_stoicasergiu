package com.example.demo.service;

import com.example.demo.client.NotificationClient;
import com.example.demo.dto.notificationdto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationClient notificationClient;

    public List<NotificationDTO> fetchUnreadNotifications(String email, String jwtToken) {
        return notificationClient.getUnreadNotifications(email, jwtToken);
    }

    public void markAllAsRead(String email) {
        notificationClient.markAllAsRead(email);
    }
}