package com.example.demo.builder.postReactionBuilder;

import com.example.demo.dto.postReactionDTO.PostReactionDTO;
import com.example.demo.entity.PostReaction;

public class PostReactionBuilder {

    public static PostReaction generateEntityFromDTO(PostReactionDTO dto) {
        return PostReaction.builder()
                .id(dto.getId())
                .authorEmail(dto.getAuthorEmail())
                .postId(dto.getPostId())
                .reactionType(dto.getReactionType())
                .build();
    }

    public static PostReactionDTO generateDTOFromEntity(PostReaction entity) {
        return PostReactionDTO.builder()
                .id(entity.getId())
                .authorEmail(entity.getAuthorEmail())
                .postId(entity.getPostId())
                .reactionType(entity.getReactionType())
                .build();
    }
}