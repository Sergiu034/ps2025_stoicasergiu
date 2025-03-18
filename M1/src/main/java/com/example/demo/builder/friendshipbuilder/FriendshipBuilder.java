package com.example.demo.builder.friendshipbuilder;

import com.example.demo.dto.friendshipDTO.FriendshipDTO;
import com.example.demo.entity.Friendship;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

public class FriendshipBuilder {

    public static FriendshipDTO  generateDTOFrimEntity(Friendship friendship) {
        return FriendshipDTO.builder()
                .id(friendship.getId())
                .user1Name(friendship.getUser1().getName())
                .user1Email(friendship.getUser2().getEmail())
                .user2Name(friendship.getUser2().getName())
                .user2Email(friendship.getUser2().getEmail())
                .status(friendship.getStatus())
                .createdAt(friendship.getCreatedAt())
                .build();
    }

    public static Friendship generateEntityFromDTO(FriendshipDTO friendshipDTO, UserRepository userRepository) {
            User user1 = userRepository.findUserByEmail(friendshipDTO.getUser1Email())
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + friendshipDTO.getUser1Email()));

            User user2 = userRepository.findUserByEmail(friendshipDTO.getUser2Email())
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + friendshipDTO.getUser2Email()));

            Friendship friendship = new Friendship();
            friendship.setUser1(user1);
            friendship.setUser2(user2);
            friendship.setStatus(friendshipDTO.getStatus());
            friendship.setCreatedAt(friendshipDTO.getCreatedAt());

            return friendship;
    }
}
