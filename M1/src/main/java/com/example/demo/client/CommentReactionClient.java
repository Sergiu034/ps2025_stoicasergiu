package com.example.demo.client;

import com.example.demo.dto.commentreactiondto.CommentReactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentReactionClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${microservice.reactions.url}")
    private String reactionServiceUrl;

    public List<CommentReactionDTO> getCommentReactionsByCommentId(Long commentId, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(reactionServiceUrl + "/api/comment-reaction/getByComment/" + commentId)
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToFlux(CommentReactionDTO.class)
                .collectList()
                .block();
    }

    public Long createOrUpdateCommentReaction(CommentReactionDTO dto, String jwtToken) {
        return webClientBuilder.build()
                .post()
                .uri(reactionServiceUrl + "/api/comment-reaction/createOrUpdate")
                .header("Authorization", jwtToken)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    public void deleteCommentReaction(Long reactionId, String jwtToken) {
        webClientBuilder.build()
                .delete()
                .uri(reactionServiceUrl + "/api/comment-reaction/delete/" + reactionId)
                .header("Authorization", jwtToken)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public CommentReactionDTO getCommentReactionById(Long reactionId, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(reactionServiceUrl + "/api/comment-reaction/" + reactionId)
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToMono(CommentReactionDTO.class)
                .block();
    }
}
