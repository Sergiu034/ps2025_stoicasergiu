package com.example.demo.client;

import com.example.demo.dto.postDTO.PostDTO;
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
public class PostClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${microservice.posts.url}")
    private String postServiceUrl;

    public List<PostDTO> getAllPosts(String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(postServiceUrl + "/api/post/getAll")
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToFlux(PostDTO.class)
                .collectList()
                .block();
    }

    public Long createPost(PostDTO dto, String jwtToken) {
        return webClientBuilder.build()
                .post()
                .uri(postServiceUrl + "/api/post/create")
                .header("Authorization", jwtToken)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    public List<PostDTO> getPostsByHashtag(String hashtag, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(postServiceUrl + "/api/post/filter/byHashtag/" + hashtag)
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToFlux(PostDTO.class)
                .collectList()
                .block();
    }

    public List<PostDTO> getPostsByText(String text, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(postServiceUrl + "/api/post/filter/byText/" + text)
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToFlux(PostDTO.class)
                .collectList()
                .block();
    }

    public List<PostDTO> getPostsByAuthor(String authorEmail, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri(postServiceUrl + "/api/post/filter/byAuthor/" + authorEmail)
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToFlux(PostDTO.class)
                .collectList()
                .block();
    }

    public List<PostDTO> searchPosts(String text, String hashtag, String authorEmail, String jwtToken) {
        StringBuilder url = new StringBuilder(postServiceUrl + "/api/post/search?");
        if (text != null) url.append("text=").append(text).append("&");
        if (hashtag != null) url.append("hashtag=").append(hashtag).append("&");
        if (authorEmail != null) url.append("authorEmail=").append(authorEmail);

        return webClientBuilder.build()
                .get()
                .uri(url.toString())
                .header("Authorization", jwtToken)
                .retrieve()
                .bodyToFlux(PostDTO.class)
                .collectList()
                .block();
    }

    public Long updatePost(PostDTO dto, String jwtToken) {
        return webClientBuilder.build()
                .put()
                .uri(postServiceUrl + "/api/post/update")
                .header("Authorization", jwtToken)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    public void deletePost(Long id, String jwtToken) {
        webClientBuilder.build()
                .delete()
                .uri(postServiceUrl + "/api/post/delete/" + id)
                .header("Authorization", jwtToken)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
