package com.example.demo.controller;

import com.example.demo.dto.commentDTO.CommentDTO;
import com.example.demo.dto.postDTO.PostDTO;
import com.example.demo.dto.postAndCommentsDTO.PostWithCommentsDTO;
import com.example.demo.service.JWTService;
import com.example.demo.service.PostAggregatorService;
import com.example.demo.client.CommentClient;
import com.example.demo.client.PostClient;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gateway/post")
@RequiredArgsConstructor
public class PostGatewayController {

    private final PostAggregatorService aggregatorService;
    private final JWTService jwtService;
    private final CommentClient commentClient;
    private final PostClient postClient;

    @GetMapping("/visible")
    public ResponseEntity<List<PostWithCommentsDTO>> getVisiblePosts(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        return ResponseEntity.ok(aggregatorService.getVisiblePostsWithComments(email, token));
    }

    @GetMapping("/visible/filter/hashtag/{hashtag}")
    public ResponseEntity<List<PostWithCommentsDTO>> filterByHashtag(
            @PathVariable String hashtag,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        return ResponseEntity.ok(aggregatorService.getFilteredPosts(email, token, hashtag, null, null));
    }

    @GetMapping("/visible/filter/text/{text}")
    public ResponseEntity<List<PostWithCommentsDTO>> filterByText(
            @PathVariable String text,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        return ResponseEntity.ok(aggregatorService.getFilteredPosts(email, token, null, text, null));
    }

    @GetMapping("/visible/filter/author/{authorEmail}")
    public ResponseEntity<List<PostWithCommentsDTO>> filterByAuthor(
            @PathVariable String authorEmail,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        return ResponseEntity.ok(aggregatorService.getFilteredPosts(email, token, null, null, authorEmail));
    }

    @PostMapping("/comment")
    public ResponseEntity<Long> createComment(
            @RequestBody CommentDTO dto,
            @RequestHeader("Authorization") String token) {
        dto.setAuthorEmail(jwtService.extractUserName(token.substring(7)));
        return ResponseEntity.ok(commentClient.createComment(dto, token));
    }

    @PostMapping("/post")
    public ResponseEntity<Long> createPost(
            @RequestBody PostDTO dto,
            @RequestHeader("Authorization") String token) {
        dto.setAuthorEmail(jwtService.extractUserName(token.substring(7)));
        return ResponseEntity.ok(postClient.createPost(dto, token));
    }

    @GetMapping("/visible/search")
    public ResponseEntity<List<PostWithCommentsDTO>> searchPosts(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String hashtag,
            @RequestParam(required = false) String authorEmail,
            @RequestHeader("Authorization") String token) {

        String email = jwtService.extractUserName(token.substring(7));
        List<PostWithCommentsDTO> result = aggregatorService.searchPosts(email, token, text, hashtag, authorEmail);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update_post/{id}")
    public ResponseEntity<Long> updatePost(@PathVariable Long id, @RequestBody PostDTO dto, @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        PostDTO originalPost = postClient.getPostById(id, token);

        if (!originalPost.getAuthorEmail().equals(email)) {
            System.out.println("Post by: " + originalPost.getAuthorEmail());
            return ResponseEntity.status(403).build();
        }

        dto.setId(id);
        dto.setAuthorEmail(email);
        return ResponseEntity.ok(postClient.updatePost(dto, token));
    }

    @DeleteMapping("/delete_post/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        PostDTO originalPost = postClient.getPostById(id, token);

        if (!originalPost.getAuthorEmail().equals(email)) {
            System.out.println("Post by :" + originalPost.getAuthorEmail());
            return ResponseEntity.status(403).build();
        }

        postClient.deletePost(id, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update_comment/{id}")
    public ResponseEntity<Long> updateComment(@PathVariable Long id, @RequestBody CommentDTO dto, @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        CommentDTO originalComment = commentClient.getCommentById(id, token);

        if (!originalComment.getAuthorEmail().equals(email)) {
            System.out.println("Comment by: " + originalComment.getAuthorEmail());
            return ResponseEntity.status(403).build();
        }

        dto.setId(id);
        dto.setAuthorEmail(email);
        return ResponseEntity.ok(commentClient.updateComment(dto, token));
    }

    @DeleteMapping("/delete_comment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        CommentDTO originalComment = commentClient.getCommentById(id, token);

        if (!originalComment.getAuthorEmail().equals(email)) {
            System.out.println("Comment by :" + originalComment.getAuthorEmail());
            return ResponseEntity.status(403).build();
        }

        commentClient.deleteComment(id, token);
        return ResponseEntity.ok().build();
    }
}
