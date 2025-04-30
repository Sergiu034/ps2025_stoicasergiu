package com.example.demo.client;

import com.example.demo.dto.blockeduserdto.BlockedUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ModerationClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${microservice.moderation.url}")
    private String moderationServiceUrl;

    public Long blockUser(BlockedUserDTO dto, String jwtToken) {
        return webClientBuilder.build()
                .post()
                .uri(moderationServiceUrl + "/api/moderation/block")
                .header("Authorization", jwtToken)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    public void unblockUser(Long userId, String jwtToken) {
        webClientBuilder.build()
                .delete()
                .uri(moderationServiceUrl + "/api/moderation/unblock/" + userId)
                .header("Authorization", jwtToken)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public boolean isUserBlocked(Long userId, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(moderationServiceUrl + "/api/moderation/is-blocked/" + userId)
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    public void deletePost(Long postId, String authorEmail, String jwtToken) {
        String url = moderationServiceUrl + "/api/moderation/delete-post/" + postId;
        if (authorEmail != null) {
            url += "?authorEmail=" + authorEmail;
        }

        webClientBuilder.build()
                .delete()
                .uri(url)
                .header("Authorization", jwtToken)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void deleteComment(Long commentId, String authorEmail, String jwtToken) {
        String url = moderationServiceUrl + "/api/moderation/delete-comment/" + commentId;
        if (authorEmail != null) {
            url += "?authorEmail=" + authorEmail;
        }

        webClientBuilder.build()
                .delete()
                .uri(url)
                .header("Authorization", jwtToken)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}

