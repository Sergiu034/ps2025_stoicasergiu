package com.example.demo.dto.commentdto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {

    private Long id;

    private String text;

    private byte[] imageData;

    private String authorEmail;

    private LocalDateTime createdAt;

    private Long postId;
}
