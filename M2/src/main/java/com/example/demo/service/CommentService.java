package com.example.demo.service;

import com.example.demo.builder.commentbuilder.CommentBuilder;
import com.example.demo.client.FriendshipClient;
import com.example.demo.dto.commentdto.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.Visibility;
import com.example.demo.errorhandler.PostException;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final FriendshipClient friendshipClient;

    public List<CommentDTO> getCommentsByPostId(Long postId, String requesterEmail, String jwtToken) throws PostException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found with id: " + postId));

        // Visibility check
        if (post.getVisibility() == Visibility.FRIENDS_ONLY) {
            if (!post.getAuthorEmail().equals(requesterEmail) &&
                    !isFriend(requesterEmail, post.getAuthorEmail(), jwtToken)) {
                throw new PostException("You are not allowed to view comments on this post.");
            }
        } else if (post.getVisibility() != Visibility.PUBLIC &&
                !post.getAuthorEmail().equals(requesterEmail)) {
            throw new PostException("You are not allowed to view comments on this post.");
        }

        return commentRepository.findAllByPost(post).stream()
                .map(CommentBuilder::generateDTOFromEntity)
                .toList();
    }

    @Transactional
    public Long createComment(CommentDTO dto, String requesterEmail, String jwtToken) throws PostException {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new PostException("Post not found with id: " + dto.getPostId()));

        if (post.getVisibility() == Visibility.FRIENDS_ONLY) {
            if (!post.getAuthorEmail().equals(requesterEmail)) {
                boolean isFriend = isFriend(requesterEmail, post.getAuthorEmail(), jwtToken);
                if (!isFriend) {
                    throw new PostException("You can only comment on posts made by your friends.");
                }
            }
        } else if (post.getVisibility() != Visibility.PUBLIC && !post.getAuthorEmail().equals(requesterEmail)) {
            throw new PostException("You are not allowed to comment on this post.");
        }

        Comment comment = CommentBuilder.generateEntityFromDTO(dto, post);
        comment.setAuthorEmail(requesterEmail);
        return commentRepository.save(comment).getId();
    }

    private boolean isFriend(String userEmail, String authorEmail, String jwtToken) {
        try {
            List<String> friendEmails = friendshipClient.getFriendEmails(jwtToken);
            return friendEmails.contains(authorEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Long updateComment(CommentDTO dto, String requesterEmail) throws PostException {
        Optional<Comment> optionalComment = commentRepository.findById(dto.getId());
        if (optionalComment.isEmpty()) {
            throw new PostException("Comment not found with id: " + dto.getId());
        }

        Comment comment = optionalComment.get();

        if (!Objects.equals(comment.getAuthorEmail(), requesterEmail)) {
            throw new PostException("You can only edit your own comments.");
        }

        comment.setText(dto.getText());
        comment.setImageData(dto.getImageData());

        return commentRepository.save(comment).getId();
    }

    public void deleteComment(Long id, String requesterEmail) throws PostException {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()) {
            throw new PostException("Comment not found with id: " + id);
        }

        Comment comment = optionalComment.get();

        if (!Objects.equals(comment.getAuthorEmail(), requesterEmail)) {
            throw new PostException("You can only delete your own comments.");
        }

        commentRepository.deleteById(id);
    }
}
