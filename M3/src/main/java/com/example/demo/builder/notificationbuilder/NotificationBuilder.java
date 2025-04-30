package com.example.demo.builder.notificationbuilder;

import com.example.demo.dto.notificationdto.NotificationDTO;
import com.example.demo.entity.Notification;

public class NotificationBuilder {

    public static Notification generateEntityFromDTO(NotificationDTO dto) {
        return Notification.builder()
                .id(dto.getId())
                .userEmail(dto.getUserEmail())
                .message(dto.getMessage())
                .createdAt(dto.getCreatedAt())
                .read(dto.isRead())
                .build();
    }

    public static NotificationDTO generateDTOFromEntity(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .userEmail(notification.getUserEmail())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .read(notification.isRead())
                .build();
    }
}