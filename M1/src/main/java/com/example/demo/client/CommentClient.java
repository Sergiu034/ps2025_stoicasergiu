package com.example.demo.client;

import com.example.demo.dto.commentDTO.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${microservice.posts.url}")
    private String postServiceUrl;

    public List<CommentDTO> getCommentsForPost(Long postId, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(postServiceUrl + "/api/comment/getByPost/" + postId)
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToFlux(CommentDTO.class)
                .collectList()
                .block();
    }

    public Long createComment(CommentDTO dto, String jwtToken) {
        return webClientBuilder.build()
                .post()
                .uri(postServiceUrl + "/api/comment/create")
                .header("Authorization", jwtToken)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    public Long updateComment(CommentDTO dto, String jwtToken) {
        return webClientBuilder.build()
                .put()
                .uri(postServiceUrl + "/api/comment/update")
                .header("Authorization", jwtToken)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    public void deleteComment(Long id, String jwtToken) {
        webClientBuilder.build()
                .delete()
                .uri(postServiceUrl + "/api/comment/delete/" + id)
                .header("Authorization", jwtToken)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public CommentDTO getCommentById(Long id, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(postServiceUrl + "/api/comment/" + id)
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToMono(CommentDTO.class)
                .block();
    }
}
