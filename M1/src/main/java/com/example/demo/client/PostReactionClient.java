package com.example.demo.client;

import com.example.demo.dto.postreactiondto.PostReactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReactionClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${microservice.reactions.url}")
    private String reactionServiceUrl;

    public List<PostReactionDTO> getPostReactionsByPostId(Long postId, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(reactionServiceUrl + "/api/post-reaction/getByPost/" + postId)
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToFlux(PostReactionDTO.class)
                .collectList()
                .block();
    }

    public Long createOrUpdatePostReaction(PostReactionDTO dto, String jwtToken) {
        return webClientBuilder.build()
                .post()
                .uri(reactionServiceUrl + "/api/post-reaction/createOrUpdate")
                .header("Authorization", jwtToken)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    public void deletePostReaction(Long reactionId, String jwtToken) {
        webClientBuilder.build()
                .delete()
                .uri(reactionServiceUrl + "/api/post-reaction/delete/" + reactionId)
                .header("Authorization", jwtToken)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public PostReactionDTO getPostReactionById(Long reactionId, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(reactionServiceUrl + "/api/post-reaction/" + reactionId)
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToMono(PostReactionDTO.class)
                .block();
    }
}
