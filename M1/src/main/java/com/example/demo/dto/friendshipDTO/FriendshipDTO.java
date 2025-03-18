package com.example.demo.dto.friendshipDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendshipDTO {

    private Long id;

    private String user1Name;
    private String user1Email;

    private String user2Name;
    private String user2Email;

    private String status;

    private LocalDateTime createdAt;
}