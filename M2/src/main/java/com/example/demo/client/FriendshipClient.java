package com.example.demo.client;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${friendship.service.url}")
    private String friendshipServiceUrl;

    public List<String> getFriendEmails(String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(friendshipServiceUrl + "/api/friendships/my/emails")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }
}
