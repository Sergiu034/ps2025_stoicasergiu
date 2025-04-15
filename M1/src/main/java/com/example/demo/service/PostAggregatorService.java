package com.example.demo.service;

import com.example.demo.errorhandler.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.demo.client.PostClient;
import com.example.demo.client.CommentClient;
import com.example.demo.dto.postAndCommentsDTO.PostWithCommentsDTO;
import com.example.demo.dto.postDTO.PostDTO;
import com.example.demo.dto.commentDTO.CommentDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostAggregatorService {

    private final PostClient postClient;
    private final CommentClient commentClient;
    private final FriendshipService friendshipService;

    public List<PostWithCommentsDTO> getVisiblePostsWithComments(String userEmail, String jwtToken) {
        List<PostDTO> posts = postClient.getAllPosts(jwtToken);

        List<String> friendEmails;
        try {
            friendEmails = friendshipService.getFriendEmails(userEmail);
        } catch (UserException e) {
            throw new RuntimeException(e);
        }

        return posts.stream()
                .filter(post -> post.getVisibility().equalsIgnoreCase("PUBLIC")
                        || friendEmails.contains(post.getAuthorEmail())
                        || post.getAuthorEmail().equalsIgnoreCase(userEmail))
                .map(post -> {
                    List<CommentDTO> comments = commentClient.getCommentsForPost(post.getId(), jwtToken);
                    return PostWithCommentsDTO.builder()
                            .post(post)
                            .comments(comments)
                            .build();
                })
                .sorted((p1, p2) -> p2.getPost().getCreatedAt().compareTo(p1.getPost().getCreatedAt()))
                .collect(Collectors.toList());
    }

    public List<PostWithCommentsDTO> getFilteredPosts(String userEmail, String jwtToken, String hashtag, String text, String authorEmail) {
        List<PostDTO> posts;

        if (hashtag != null) {
            posts = postClient.getPostsByHashtag(hashtag, jwtToken);
        } else if (text != null) {
            posts = postClient.getPostsByText(text, jwtToken);
        } else if (authorEmail != null) {
            posts = postClient.getPostsByAuthor(authorEmail, jwtToken);
        } else {
            posts = postClient.getAllPosts(jwtToken);
        }

        List<String> friendEmails;
        try {
            friendEmails = friendshipService.getFriendEmails(userEmail);
        } catch (UserException e) {
            throw new RuntimeException(e);
        }

        return posts.stream()
                .filter(post -> post.getVisibility().equalsIgnoreCase("PUBLIC")
                        || friendEmails.contains(post.getAuthorEmail())
                        || post.getAuthorEmail().equalsIgnoreCase(userEmail))
                .map(post -> {
                    List<CommentDTO> comments = commentClient.getCommentsForPost(post.getId(), jwtToken);
                    return PostWithCommentsDTO.builder()
                            .post(post)
                            .comments(comments)
                            .build();
                })
                .sorted((p1, p2) -> p2.getPost().getCreatedAt().compareTo(p1.getPost().getCreatedAt()))
                .collect(Collectors.toList());
    }

    public List<PostWithCommentsDTO> searchPosts(String userEmail, String jwtToken, String text, String hashtag, String authorEmail) {
        List<PostDTO> posts = postClient.searchPosts(text, hashtag, authorEmail, jwtToken);

        List<String> friendEmails;
        try {
            friendEmails = friendshipService.getFriendEmails(userEmail);
        } catch (UserException e) {
            throw new RuntimeException(e);
        }

        return posts.stream()
                .filter(post -> post.getVisibility().equalsIgnoreCase("PUBLIC")
                        || friendEmails.contains(post.getAuthorEmail())
                        || post.getAuthorEmail().equalsIgnoreCase(userEmail))
                .map(post -> {
                    List<CommentDTO> comments = commentClient.getCommentsForPost(post.getId(), jwtToken);
                    return PostWithCommentsDTO.builder()
                            .post(post)
                            .comments(comments)
                            .build();
                })
                .sorted((p1, p2) -> p2.getPost().getCreatedAt().compareTo(p1.getPost().getCreatedAt()))
                .collect(Collectors.toList());
    }
}
