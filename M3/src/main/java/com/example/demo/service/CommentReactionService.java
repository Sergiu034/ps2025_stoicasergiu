package com.example.demo.service;

import com.example.demo.builder.commentReactionBuilder.CommentReactionBuilder;
import com.example.demo.dto.commentReactionDTO.CommentReactionDTO;
import com.example.demo.entity.CommentReaction;
import com.example.demo.errorhandler.CommentReactionException;
import com.example.demo.repository.CommentReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentReactionService {

    private final CommentReactionRepository commentReactionRepository;

    @Transactional
    public Long reactToComment(CommentReactionDTO dto) {
        Optional<CommentReaction> optionalReaction = commentReactionRepository
                .findByAuthorEmailAndCommentId(dto.getAuthorEmail(), dto.getCommentId());

        CommentReaction reaction;
        if (optionalReaction.isPresent()) {
            System.out.println("This reaction has already been set");
            throw new CommentReactionException("This reaction has already been set");
        } else {
            reaction = CommentReactionBuilder.generateEntityFromDTO(dto);
        }

        return commentReactionRepository.save(reaction).getId();
    }

    public List<CommentReactionDTO> getReactionsByCommentId(Long commentId) {
        return commentReactionRepository.findAllByCommentId(commentId).stream()
                .map(CommentReactionBuilder::generateDTOFromEntity)
                .toList();
    }

    public void deleteReaction(Long id) throws CommentReactionException {
        Optional<CommentReaction> optionalReaction = commentReactionRepository.findById(id);
        if (optionalReaction.isEmpty()) {
            throw new CommentReactionException("Reaction not found with id: " + id);
        }
        commentReactionRepository.deleteById(id);
    }

    public CommentReactionDTO getReactionById(Long id) throws CommentReactionException {
        CommentReaction reaction = commentReactionRepository.findById(id)
                .orElseThrow(() -> new CommentReactionException("Reaction not found with id: " + id));
        return CommentReactionBuilder.generateDTOFromEntity(reaction);
    }
}
