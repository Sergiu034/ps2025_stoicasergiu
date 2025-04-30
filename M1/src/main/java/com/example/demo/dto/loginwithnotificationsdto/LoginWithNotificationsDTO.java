package com.example.demo.dto.loginwithnotificationsdto;

import com.example.demo.dto.notificationdto.NotificationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginWithNotificationsDTO {
    private String jwtToken;
    private List<NotificationDTO> unreadNotifications;
}
