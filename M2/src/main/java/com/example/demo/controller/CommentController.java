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
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId,
                                                              @RequestHeader("Authorization") String authHeader) throws PostException {
        String jwt = authHeader.replace("Bearer ", "");
        String requesterEmail = JWT.decode(jwt).getSubject();

        List<CommentDTO> comments = commentService.getCommentsByPostId(postId, requesterEmail, jwt);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO dto,
                                           @RequestHeader("Authorization") String authHeader) throws PostException {
        String jwt = authHeader.replace("Bearer ", "");
        String requesterEmail = JWT.decode(jwt).getSubject();

        Long commentId = commentService.createComment(dto, requesterEmail, jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateComment(@RequestBody CommentDTO dto,
                                           @RequestHeader("Authorization") String authHeader) throws PostException {
        String jwt = authHeader.replace("Bearer ", "");
        String requesterEmail = JWT.decode(jwt).getSubject();

        Long updatedId = commentService.updateComment(dto, requesterEmail);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id,
                                           @RequestHeader("Authorization") String authHeader) throws PostException {
        String jwt = authHeader.replace("Bearer ", "");
        String requesterEmail = JWT.decode(jwt).getSubject();

        commentService.deleteComment(id, requesterEmail);
        return ResponseEntity.ok().build();
    }
}
