package com.example.demo.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ModerationClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${microservice.posts.url}")
    private String postServiceUrl;

    @Value("${microservice.comments.url}")
    private String commentServiceUrl;

    public void deletePost(Long postId) {
        webClientBuilder.build()
                .delete()
                .uri(postServiceUrl + "/api/post/delete/" + postId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void deleteComment(Long commentId) {
        webClientBuilder.build()
                .delete()
                .uri(commentServiceUrl + "/api/comment/delete/" + commentId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
