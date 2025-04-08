package com.example.demo.builder.postbuilder;

import com.example.demo.dto.postdto.PostDTO;
import com.example.demo.entity.Hashtag;
import com.example.demo.entity.Post;
import com.example.demo.entity.Visibility;

import java.time.LocalDateTime;
import java.util.Set;

public class PostBuilder {

    public static Post generateEntityFromDTO(PostDTO postDTO, Set<Hashtag> hashtagEntities) {
        return Post.builder()
                .id(postDTO.getId())
                .text(postDTO.getText())
                .imageData(postDTO.getImageData())
                .authorEmail(postDTO.getAuthorEmail())
                .createdAt(LocalDateTime.now())
                .hashtags(hashtagEntities)
                .visibility(Visibility.valueOf(postDTO.getVisibility().toUpperCase()))
                .build();
    }

    public static PostDTO generateDTOFromEntity(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .text(post.getText())
                .imageData(post.getImageData())
                .authorEmail(post.getAuthorEmail())
                .createdAt(post.getCreatedAt())
                .hashtags(
                        post.getHashtags()
                                .stream()
                                .map(hashtag -> hashtag.getName())
                                .toList()
                )
                .visibility(post.getVisibility().name())
                .build();
    }
}
