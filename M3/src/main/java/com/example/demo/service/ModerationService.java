package com.example.demo.service;

import com.example.demo.builder.notificationbuilder.NotificationBuilder;
import com.example.demo.client.ModerationClient;
import com.example.demo.dto.notificationdto.NotificationDTO;
import com.example.demo.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ModerationService {

    private final ModerationClient moderationClient;
    private final NotificationService notificationService;

    public void deletePost(Long postId, String authorEmail) {
        moderationClient.deletePost(postId);

        Notification notification = new Notification();
        notification.setUserEmail(authorEmail);
        notification.setMessage("Your post has been removed by a moderator.");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        NotificationDTO notificationDTO = NotificationBuilder.generateDTOFromEntity(notification);
        notificationService.sendNotification(notificationDTO);
    }

    public void deleteComment(Long commentId, String authorEmail) {
        moderationClient.deleteComment(commentId);

        Notification notification = new Notification();
        notification.setUserEmail(authorEmail);
        notification.setMessage("Your comment has been removed by a moderator.");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        NotificationDTO notificationDTO = NotificationBuilder.generateDTOFromEntity(notification);
        notificationService.sendNotification(notificationDTO);
    }
}
