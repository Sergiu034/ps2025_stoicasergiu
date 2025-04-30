package com.example.demo.dto.notificationdto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {

    private Long id;

    private String userEmail;

    private String message;

    private LocalDateTime createdAt;

    private boolean read;
}
