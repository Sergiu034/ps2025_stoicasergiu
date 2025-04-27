package com.example.demo.controller;

import com.example.demo.dto.commentReactionDTO.CommentReactionDTO;
import com.example.demo.errorhandler.CommentReactionException;
import com.example.demo.service.CommentReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment-reaction")
@RequiredArgsConstructor
public class CommentReactionController {

    private final CommentReactionService commentReactionService;

    @GetMapping("/home")
    public String greet(){
        return "Hello World !";
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentReactionDTO> getReactionById(@PathVariable Long id) throws CommentReactionException {
        CommentReactionDTO reaction = commentReactionService.getReactionById(id);
        return ResponseEntity.ok(reaction);
    }

    @GetMapping("/getByComment/{commentId}")
    public ResponseEntity<List<CommentReactionDTO>> getReactionsByComment(@PathVariable Long commentId) {
        List<CommentReactionDTO> reactions = commentReactionService.getReactionsByCommentId(commentId);
        return ResponseEntity.ok(reactions);
    }

    @PostMapping("/createOrUpdate")
    public ResponseEntity<Long> createOrUpdateReaction(@RequestBody CommentReactionDTO dto) {
        Long reactionId = commentReactionService.reactToComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reactionId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long id) throws CommentReactionException {
        commentReactionService.deleteReaction(id);
        return ResponseEntity.ok().build();
    }
}
