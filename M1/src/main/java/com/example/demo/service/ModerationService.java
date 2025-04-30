package com.example.demo.service;

import com.example.demo.client.ModerationClient;
import com.example.demo.dto.blockeduserdto.BlockedUserDTO;
import com.example.demo.entity.User;
import com.example.demo.errorhandler.UserException;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModerationService {

    private final ModerationClient moderationClient;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    private final Long ADMIN_ROLE_ID = 2L;

    public Long blockUser(BlockedUserDTO dto, String jwtToken) throws UserException {
        User user = getUserFromToken(jwtToken);

        if (!isAdmin(user)) {
            throw new UserException("You are not authorized to block users.");
        }

        return moderationClient.blockUser(dto, jwtToken);
    }

    public void unblockUser(Long userId, String jwtToken) throws UserException {
        User user = getUserFromToken(jwtToken);

        if (!isAdmin(user)) {
            throw new UserException("You are not authorized to unblock users.");
        }

        moderationClient.unblockUser(userId, jwtToken);
    }

    public boolean isUserBlocked(Long userId, String jwtToken) {
        return moderationClient.isUserBlocked(userId, jwtToken);
    }

    public void deletePost(Long postId, String authorEmail, String jwtToken) throws UserException {
        User user = getUserFromToken(jwtToken);

        if (!isAdmin(user)) {
            throw new UserException("You are not authorized to delete posts.");
        }

        moderationClient.deletePost(postId, authorEmail, jwtToken);
    }

    public void deleteComment(Long commentId, String authorEmail, String jwtToken) throws UserException {
        User user = getUserFromToken(jwtToken);

        if (!isAdmin(user)) {
            throw new UserException("You are not authorized to delete comments.");
        }

        moderationClient.deleteComment(commentId, authorEmail, jwtToken);
    }

    private User getUserFromToken(String jwtToken) throws UserException {
        String email = jwtService.extractUserName(jwtToken.substring(7));
        Optional<User> user = userRepository.findUserByEmail(email);

        if (user.isEmpty()) {
            throw new UserException("User not found with email: " + email);
        }

        return user.get();
    }

    private boolean isAdmin(User user) {
        return user.getRole().getName().equalsIgnoreCase("ADMIN");
    }

}
