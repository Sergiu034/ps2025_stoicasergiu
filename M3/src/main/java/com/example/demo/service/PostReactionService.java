package com.example.demo.service;

import com.example.demo.builder.postReactionBuilder.PostReactionBuilder;
import com.example.demo.dto.postReactionDTO.PostReactionDTO;
import com.example.demo.entity.PostReaction;
import com.example.demo.errorhandler.CommentReactionException;
import com.example.demo.errorhandler.PostReactionException;
import com.example.demo.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostReactionService {

    private final PostReactionRepository postReactionRepository;

    @Transactional
    public Long reactToPost(PostReactionDTO dto) {
        Optional<PostReaction> optionalReaction = postReactionRepository
                .findByAuthorEmailAndPostId(dto.getAuthorEmail(), dto.getPostId());

        PostReaction reaction;
        if (optionalReaction.isPresent()) {
            System.out.println("This reaction has already been set");
            throw new PostReactionException("This reaction has already been set");
        } else {
            reaction = PostReactionBuilder.generateEntityFromDTO(dto);
        }

        return postReactionRepository.save(reaction).getId();
    }

    public List<PostReactionDTO> getReactionsByPostId(Long postId) {
        return postReactionRepository.findAllByPostId(postId).stream()
                .map(PostReactionBuilder::generateDTOFromEntity)
                .toList();
    }

    public void deleteReaction(Long id) throws PostReactionException {
        Optional<PostReaction> optionalReaction = postReactionRepository.findById(id);
        if (optionalReaction.isEmpty()) {
            throw new PostReactionException("Reaction not found with id: " + id);
        }
        postReactionRepository.deleteById(id);
    }

    public PostReactionDTO getReactionById(Long id) throws PostReactionException {
        PostReaction reaction = postReactionRepository.findById(id)
                .orElseThrow(() -> new PostReactionException("Reaction not found with id: " + id));
        return PostReactionBuilder.generateDTOFromEntity(reaction);
    }
}