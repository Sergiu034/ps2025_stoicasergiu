package com.example.demo.client;

import com.example.demo.dto.notificationdto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${microservice.notifications.url}")
    private String notificationServiceUrl;

    public List<NotificationDTO> getUnreadNotifications(String userEmail, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(notificationServiceUrl + "/api/notifications/unread/" + userEmail)
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToFlux(NotificationDTO.class)
                .collectList()
                .block();
    }

    public void markAllAsRead(String email) {
        webClientBuilder.build()
                .put()
                .uri(notificationServiceUrl + "/api/notifications/mark-as-read/" + email)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}
