package com.example.demo.dto.commentReactionDTO;

import com.example.demo.entity.ReactionType;
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
    private ReactionType reactionType;
}
