package com.example.demo.service;

import com.example.demo.client.CommentReactionClient;
import com.example.demo.client.PostReactionClient;
import com.example.demo.dto.commentreactiondto.CommentReactionDTO;
import com.example.demo.dto.commentswithreactionsdto.CommentWithReactionsDTO;
import com.example.demo.dto.postreactiondto.PostReactionDTO;
import com.example.demo.errorhandler.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.demo.client.PostClient;
import com.example.demo.client.CommentClient;
import com.example.demo.dto.postAndCommentsDTO.PostWithCommentsDTO;
import com.example.demo.dto.postandcommentswithreactionsdto.PostWithCommentsAndReactionsDTO;
import com.example.demo.dto.postDTO.PostDTO;
import com.example.demo.dto.commentDTO.CommentDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostAggregatorService {

    private final PostClient postClient;
    private final CommentClient commentClient;
    private final FriendshipService friendshipService;

    private final PostReactionClient postReactionClient;
    private final CommentReactionClient commentReactionClient;

    public List<PostWithCommentsAndReactionsDTO> getVisiblePostsWithComments(String userEmail, String jwtToken) {
        List<PostDTO> posts = postClient.getAllPosts(jwtToken);
        return filterAndBuildPosts(posts, userEmail, jwtToken);
    }

    public List<PostWithCommentsAndReactionsDTO> getFilteredPosts(String userEmail, String jwtToken, String hashtag, String text, String authorEmail) {
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

        return filterAndBuildPosts(posts, userEmail, jwtToken);
    }

    public List<PostWithCommentsAndReactionsDTO> searchPosts(String userEmail, String jwtToken, String text, String hashtag, String authorEmail) {
        List<PostDTO> posts = postClient.searchPosts(text, hashtag, authorEmail, jwtToken);
        return filterAndBuildPosts(posts, userEmail, jwtToken);
    }

    ///  PRIVATE FUNCTIONS

    private List<PostWithCommentsAndReactionsDTO> filterAndBuildPosts(List<PostDTO> posts, String userEmail, String jwtToken) {
        List<String> friendEmails = getFriendEmailsSafe(userEmail);

        return posts.stream()
                .filter(post -> isPostVisibleToUser(post, friendEmails, userEmail))
                .map(post -> buildPostWithCommentsAndReactions(post, jwtToken))
                .sorted((p1, p2) -> p2.getPost().getCreatedAt().compareTo(p1.getPost().getCreatedAt()))
                .collect(Collectors.toList());
    }

    private PostWithCommentsAndReactionsDTO buildPostWithCommentsAndReactions(PostDTO post, String jwtToken) {
        // Post reactions
        List<PostReactionDTO> postReactions = postReactionClient.getPostReactionsByPostId(post.getId(), jwtToken);
        Map<String, Long> postReactionCounts = postReactions.stream()
                .collect(Collectors.groupingBy(PostReactionDTO::getReactionType, Collectors.counting()));

        // Comments with reactions
        List<CommentWithReactionsDTO> comments = commentClient.getCommentsForPost(post.getId(), jwtToken).stream()
                .map(comment -> {
                    List<CommentReactionDTO> commentReactions = commentReactionClient.getCommentReactionsByCommentId(comment.getId(), jwtToken);
                    Map<String, Long> commentReactionCounts = commentReactions.stream()
                            .collect(Collectors.groupingBy(CommentReactionDTO::getReactionType, Collectors.counting()));
                    return CommentWithReactionsDTO.builder()
                            .comment(comment)
                            .reactions(commentReactionCounts)
                            .build();
                })
                .sorted((c1, c2) -> Long.compare(
                        c2.getReactions().values().stream().mapToLong(Long::longValue).sum(),
                        c1.getReactions().values().stream().mapToLong(Long::longValue).sum()
                ))
                .collect(Collectors.toList());

        return PostWithCommentsAndReactionsDTO.builder()
                .post(post)
                .comments(comments)
                .reactions(postReactionCounts)
                .build();
    }

    private List<String> getFriendEmailsSafe(String userEmail) {
        try {
            return friendshipService.getFriendEmails(userEmail);
        } catch (UserException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isPostVisibleToUser(PostDTO post, List<String> friendEmails, String userEmail) {
        return post.getVisibility().equalsIgnoreCase("PUBLIC")
                || friendEmails.contains(post.getAuthorEmail())
                || post.getAuthorEmail().equalsIgnoreCase(userEmail);
    }
}
