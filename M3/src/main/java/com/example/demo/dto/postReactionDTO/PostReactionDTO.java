package com.example.demo.dto.postReactionDTO;

import com.example.demo.entity.ReactionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostReactionDTO {
    private Long id;
    private String authorEmail;
    private Long postId;
    private ReactionType reactionType;
}