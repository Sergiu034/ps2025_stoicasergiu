package com.example.demo.builder.commentbuilder;

import com.example.demo.dto.commentdto.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;

import java.time.LocalDateTime;

public class CommentBuilder {

    public static Comment generateEntityFromDTO(CommentDTO dto, Post post) {
        return Comment.builder()
                .id(dto.getId())
                .text(dto.getText())
                .imageData(dto.getImageData())
                .createdAt(LocalDateTime.now())
                .authorEmail(dto.getAuthorEmail())
                .post(post)
                .build();
    }

    public static CommentDTO generateDTOFromEntity(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .imageData(comment.getImageData())
                .createdAt(comment.getCreatedAt())
                .authorEmail(comment.getAuthorEmail())
                .postId(comment.getPost().getId())
                .build();
    }
}
