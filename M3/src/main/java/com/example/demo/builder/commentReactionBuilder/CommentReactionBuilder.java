package com.example.demo.builder.commentReactionBuilder;

import com.example.demo.dto.commentReactionDTO.CommentReactionDTO;
import com.example.demo.entity.CommentReaction;

public class CommentReactionBuilder {

    public static CommentReaction generateEntityFromDTO(CommentReactionDTO dto) {
        return CommentReaction.builder()
                .id(dto.getId())
                .authorEmail(dto.getAuthorEmail())
                .commentId(dto.getCommentId())
                .reactionType(dto.getReactionType())
                .build();
    }

    public static CommentReactionDTO generateDTOFromEntity(CommentReaction entity) {
        return CommentReactionDTO.builder()
                .id(entity.getId())
                .authorEmail(entity.getAuthorEmail())
                .commentId(entity.getCommentId())
                .reactionType(entity.getReactionType())
                .build();
    }
}
