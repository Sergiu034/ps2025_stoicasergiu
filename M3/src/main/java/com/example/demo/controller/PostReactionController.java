package com.example.demo.controller;

import com.example.demo.dto.postReactionDTO.PostReactionDTO;
import com.example.demo.errorhandler.PostReactionException;
import com.example.demo.service.PostReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post-reaction")
@RequiredArgsConstructor
public class PostReactionController {

    private final PostReactionService postReactionService;

    @GetMapping("/{id}")
    public ResponseEntity<PostReactionDTO> getReactionById(@PathVariable Long id) throws PostReactionException {
        PostReactionDTO reaction = postReactionService.getReactionById(id);
        return ResponseEntity.ok(reaction);
    }

    @GetMapping("/getByPost/{postId}")
    public ResponseEntity<List<PostReactionDTO>> getReactionsByPost(@PathVariable Long postId) {
        List<PostReactionDTO> reactions = postReactionService.getReactionsByPostId(postId);
        return ResponseEntity.ok(reactions);
    }

    @PostMapping("/createOrUpdate")
    public ResponseEntity<Long> createOrUpdateReaction(@RequestBody PostReactionDTO dto) {
        Long reactionId = postReactionService.reactToPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reactionId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long id) throws PostReactionException {
        postReactionService.deleteReaction(id);
        return ResponseEntity.ok().build();
    }
}
