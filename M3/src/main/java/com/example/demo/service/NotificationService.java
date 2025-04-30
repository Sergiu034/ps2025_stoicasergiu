package com.example.demo.service;

import com.example.demo.builder.notificationbuilder.NotificationBuilder;
import com.example.demo.dto.notificationdto.NotificationDTO;
import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Long sendNotification(NotificationDTO notificationDTO) {
        notificationDTO.setCreatedAt(LocalDateTime.now());
        notificationDTO.setRead(false);

        Notification notification = NotificationBuilder.generateEntityFromDTO(notificationDTO);
        return notificationRepository.save(notification).getId();
    }

    public List<NotificationDTO> getUnreadNotifications(String userEmail) {
        return notificationRepository.findAllByUserEmailAndReadFalse(userEmail)
                .stream()
                .map(NotificationBuilder::generateDTOFromEntity)
                .collect(Collectors.toList());
    }

    public void markAllAsRead(String userEmail) {
        List<Notification> unread = notificationRepository.findAllByUserEmailAndReadFalse(userEmail);

        unread.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unread);
    }
}
