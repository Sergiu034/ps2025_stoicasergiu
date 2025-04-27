package com.example.demo.dto.commentswithreactionsdto;

import com.example.demo.dto.commentDTO.CommentDTO;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentWithReactionsDTO {
    private CommentDTO comment;
    private Map<String, Long> reactions;
}