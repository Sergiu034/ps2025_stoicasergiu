package com.example.demo.dto.postandcommentswithreactionsdto;

import com.example.demo.dto.commentswithreactionsdto.CommentWithReactionsDTO;
import com.example.demo.dto.postDTO.PostDTO;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostWithCommentsAndReactionsDTO {
    private PostDTO post;
    private List<CommentWithReactionsDTO> comments;
    private Map<String, Long> reactions;
}