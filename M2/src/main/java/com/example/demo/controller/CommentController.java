package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.example.demo.dto.commentdto.CommentDTO;
import com.example.demo.errorhandler.PostException;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/getByPost/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) throws PostException {
        List<CommentDTO> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createComment(@RequestBody CommentDTO dto) throws PostException {
        Long commentId = commentService.createComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @PutMapping("/update")
    public ResponseEntity<Long> updateComment(@RequestBody CommentDTO dto) throws PostException {
        Long updatedId = commentService.updateComment(dto);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) throws PostException {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}
