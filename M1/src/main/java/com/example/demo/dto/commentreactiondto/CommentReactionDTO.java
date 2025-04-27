package com.example.demo.dto.commentreactiondto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReactionDTO {
    private Long id;
    private String authorEmail;
    private Long commentId;
    private String reactionType;
}
