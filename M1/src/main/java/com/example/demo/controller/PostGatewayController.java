package com.example.demo.controller;

import com.example.demo.client.CommentReactionClient;
import com.example.demo.dto.commentDTO.CommentDTO;
import com.example.demo.dto.commentreactiondto.CommentReactionDTO;
import com.example.demo.dto.postDTO.PostDTO;
import com.example.demo.dto.postAndCommentsDTO.PostWithCommentsDTO;
import com.example.demo.dto.postreactiondto.PostReactionDTO;
import com.example.demo.service.JWTService;
import com.example.demo.service.PostAggregatorService;
import com.example.demo.client.CommentClient;
import com.example.demo.client.PostClient;
import com.example.demo.client.PostReactionClient;
import com.example.demo.dto.postandcommentswithreactionsdto.PostWithCommentsAndReactionsDTO;


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

    private final PostReactionClient postReactionClient;
    private final CommentReactionClient commentReactionClient;

    @GetMapping("/visible")
    public ResponseEntity<List<PostWithCommentsAndReactionsDTO>> getVisiblePosts(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        return ResponseEntity.ok(aggregatorService.getVisiblePostsWithComments(email, token));
    }

    @GetMapping("/visible/filter/hashtag/{hashtag}")
    public ResponseEntity<List<PostWithCommentsAndReactionsDTO>> filterByHashtag(
            @PathVariable String hashtag,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        return ResponseEntity.ok(aggregatorService.getFilteredPosts(email, token, hashtag, null, null));
    }

    @GetMapping("/visible/filter/text/{text}")
    public ResponseEntity<List<PostWithCommentsAndReactionsDTO>> filterByText(
            @PathVariable String text,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        return ResponseEntity.ok(aggregatorService.getFilteredPosts(email, token, null, text, null));
    }

    @GetMapping("/visible/filter/author/{authorEmail}")
    public ResponseEntity<List<PostWithCommentsAndReactionsDTO>> filterByAuthor(
            @PathVariable String authorEmail,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        return ResponseEntity.ok(aggregatorService.getFilteredPosts(email, token, null, null, authorEmail));
    }

    @GetMapping("/visible/search")
    public ResponseEntity<List<PostWithCommentsAndReactionsDTO>> searchPosts(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String hashtag,
            @RequestParam(required = false) String authorEmail,
            @RequestHeader("Authorization") String token) {

        String email = jwtService.extractUserName(token.substring(7));
        List<PostWithCommentsAndReactionsDTO> result = aggregatorService.searchPosts(email, token, text, hashtag, authorEmail);
        return ResponseEntity.ok(result);
    }

    ///  POSTS REQUESTS

    @PostMapping("/post")
    public ResponseEntity<Long> createPost(
            @RequestBody PostDTO dto,
            @RequestHeader("Authorization") String token) {
        dto.setAuthorEmail(jwtService.extractUserName(token.substring(7)));
        return ResponseEntity.ok(postClient.createPost(dto, token));
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

    /// REACTIONS TO POSTS

    @PostMapping("/react_to_post")
    public ResponseEntity<Long> reactToPost(
            @RequestBody PostReactionDTO dto,
            @RequestHeader("Authorization") String token) {
        dto.setAuthorEmail(jwtService.extractUserName(token.substring(7)));
        return ResponseEntity.ok(postReactionClient.createOrUpdatePostReaction(dto, token));
    }

    @DeleteMapping("/delete_post_reaction/{id}")
    public ResponseEntity<Void> deletePostReaction(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        PostReactionDTO reaction = postReactionClient.getPostReactionById(id, token);

        if (!reaction.getAuthorEmail().equals(email)) {
            System.out.println("Reaction created by: " + reaction.getAuthorEmail());
            return ResponseEntity.status(403).build();
        }

        postReactionClient.deletePostReaction(id, token);
        return ResponseEntity.ok().build();
    }

    /// COMMENTS REQUESTS

    @PostMapping("/comment")
    public ResponseEntity<Long> createComment(
            @RequestBody CommentDTO dto,
            @RequestHeader("Authorization") String token) {
        dto.setAuthorEmail(jwtService.extractUserName(token.substring(7)));
        return ResponseEntity.ok(commentClient.createComment(dto, token));
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

    /// COMMENTS REACTIONS

    @PostMapping("/react_to_comment")
    public ResponseEntity<Long> reactToComment(
            @RequestBody CommentReactionDTO dto,
            @RequestHeader("Authorization") String token) {
        dto.setAuthorEmail(jwtService.extractUserName(token.substring(7)));
        return ResponseEntity.ok(commentReactionClient.createOrUpdateCommentReaction(dto, token));
    }

    @DeleteMapping("/delete_comment_reaction/{id}")
    public ResponseEntity<Void> deleteCommentReaction(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        String email = jwtService.extractUserName(token.substring(7));
        CommentReactionDTO reaction = commentReactionClient.getCommentReactionById(id, token);

        if (!reaction.getAuthorEmail().equals(email)) {
            System.out.println("Reaction created by: " + reaction.getAuthorEmail());
            return ResponseEntity.status(403).build();
        }

        commentReactionClient.deleteCommentReaction(id, token);
        return ResponseEntity.ok().build();
    }
}
