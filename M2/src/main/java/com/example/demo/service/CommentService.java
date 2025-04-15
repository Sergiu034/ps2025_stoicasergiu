package com.example.demo.service;

import com.example.demo.builder.commentbuilder.CommentBuilder;
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

    public List<CommentDTO> getCommentsByPostId(Long postId) throws PostException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found with id: " + postId));

        return commentRepository.findAllByPost(post).stream()
                .map(CommentBuilder::generateDTOFromEntity)
                .toList();
    }

    @Transactional
    public Long createComment(CommentDTO dto) throws PostException {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new PostException("Post not found with id: " + dto.getPostId()));

        Comment comment = CommentBuilder.generateEntityFromDTO(dto, post);
        return commentRepository.save(comment).getId();
    }

    @Transactional
    public Long updateComment(CommentDTO dto) throws PostException {
        Optional<Comment> optionalComment = commentRepository.findById(dto.getId());
        if (optionalComment.isEmpty()) {
            throw new PostException("Comment not found with id: " + dto.getId());
        }

        Comment comment = optionalComment.get();
        comment.setText(dto.getText());
        comment.setImageData(dto.getImageData());

        return commentRepository.save(comment).getId();
    }

    public void deleteComment(Long id) throws PostException {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()) {
            throw new PostException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }
}
