package com.example.demo.dto.postAndCommentsDTO;

import com.example.demo.dto.postDTO.PostDTO;
import com.example.demo.dto.commentDTO.CommentDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostWithCommentsDTO {
    private PostDTO post;
    private List<CommentDTO> comments;
}