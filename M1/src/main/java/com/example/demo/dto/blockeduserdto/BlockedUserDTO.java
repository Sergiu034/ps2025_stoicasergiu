package com.example.demo.dto.blockeduserdto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockedUserDTO {
    private Long userId;
    private String userEmail;
    private String reason;
    private LocalDateTime blockedAt;
}