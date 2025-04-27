package com.example.demo.repository;

import com.example.demo.entity.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {

    Optional<PostReaction> findByAuthorEmailAndPostId(String authorEmail, Long postId);

    List<PostReaction> findAllByPostId(Long postId);
}
