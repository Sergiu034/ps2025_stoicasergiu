package com.example.demo.controller;

import com.example.demo.service.ModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/moderation")
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationService moderationService;

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<?> deletePostByModerator(@PathVariable("postId") Long postId,
                                                   @RequestParam("authorEmail") String authorEmail) {
        moderationService.deletePost(postId, authorEmail);
        return new ResponseEntity<>("Post deleted by moderator.", HttpStatus.OK);
    }

    @DeleteMapping("/delete-comment/{commentId}")
    public ResponseEntity<?> deleteCommentByModerator(@PathVariable("commentId") Long commentId,
                                                      @RequestParam("authorEmail") String authorEmail) {
        moderationService.deleteComment(commentId, authorEmail);
        return new ResponseEntity<>("Comment deleted by moderator.", HttpStatus.OK);
    }
}
