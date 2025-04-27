package com.example.demo.repository;

import com.example.demo.entity.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {

    Optional<CommentReaction> findByAuthorEmailAndCommentId(String authorEmail, Long commentId);

    List<CommentReaction> findAllByCommentId(Long commentId);
}