package com.example.demo.controller;

import com.example.demo.dto.blockeduserdto.BlockedUserDTO;
import com.example.demo.errorhandler.UserException;
import com.example.demo.service.ModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gateway/moderation")
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationService moderationService;

    @PostMapping("/block")
    public ResponseEntity<Long> blockUser(@RequestBody BlockedUserDTO dto,
                                          @RequestHeader("Authorization") String token) throws UserException {
        Long id = moderationService.blockUser(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @DeleteMapping("/unblock/{userId}")
    public ResponseEntity<Void> unblockUser(@PathVariable Long userId,
                                            @RequestHeader("Authorization") String token) throws UserException {
        moderationService.unblockUser(userId, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<?> deletePostByModerator(@PathVariable Long postId,
                                                   @RequestParam("authorEmail") String authorEmail,
                                                   @RequestHeader("Authorization") String token)
            throws UserException {
        moderationService.deletePost(postId, authorEmail, token);
        return new ResponseEntity<>("Post deleted by moderator.", HttpStatus.OK);
    }

    @DeleteMapping("/delete-comment/{commentId}")
    public ResponseEntity<?> deleteCommentByModerator(@PathVariable Long commentId,
                                                      @RequestParam("authorEmail") String authorEmail,
                                                      @RequestHeader("Authorization") String token)
            throws UserException {
        moderationService.deleteComment(commentId, authorEmail, token);
        return new ResponseEntity<>("Comment deleted by moderator.", HttpStatus.OK);
    }
}
