package com.example.demo.builder.blockeduserbuilder;

import com.example.demo.dto.blockeduserdto.BlockedUserDTO;
import com.example.demo.entity.BlockedUser;

import java.time.LocalDateTime;

public class BlockedUserBuilder {

    public static BlockedUser generateEntityFromDTO(BlockedUserDTO dto) {
        return BlockedUser.builder()
                .userId(dto.getUserId())
                .userEmail(dto.getUserEmail())
                .reason(dto.getReason())
                .blockedAt(LocalDateTime.now())
                .build();
    }

    public static BlockedUserDTO generateDTOFromEntity(BlockedUser blockedUser) {
        return BlockedUserDTO.builder()
                .userId(blockedUser.getUserId())
                .userEmail(blockedUser.getUserEmail())
                .reason(blockedUser.getReason())
                .blockedAt(blockedUser.getBlockedAt())
                .build();
    }
}
