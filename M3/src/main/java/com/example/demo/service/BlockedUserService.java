package com.example.demo.service;

import com.example.demo.dto.blockeduserdto.BlockedUserDTO;
import com.example.demo.dto.notificationdto.NotificationDTO;
import com.example.demo.entity.BlockedUser;
import com.example.demo.entity.Notification;
import com.example.demo.errorhandler.UserException;
import com.example.demo.repository.BlockedUserRepository;
import com.example.demo.builder.blockeduserbuilder.BlockedUserBuilder;
import com.example.demo.builder.notificationbuilder.NotificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlockedUserService {

    private final BlockedUserRepository blockedUserRepository;
    private final NotificationService notificationService;

    public Long blockUser(BlockedUserDTO dto) throws UserException {
        Optional<BlockedUser> existing = blockedUserRepository.findByUserId(dto.getUserId());
        if (existing.isPresent()) {
            throw new UserException("User is already blocked with ID: " + dto.getUserId());
        }

        BlockedUser blocked = BlockedUserBuilder.generateEntityFromDTO(dto);
        Long blockedId = blockedUserRepository.save(blocked).getId();

        Notification notification = new Notification();
        notification.setUserEmail(dto.getUserEmail());
        notification.setMessage("Your account has been blocked: " + dto.getReason());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        NotificationDTO notificationDTO = NotificationBuilder.generateDTOFromEntity(notification);
        notificationService.sendNotification(notificationDTO);

        return blockedId;
    }

    public void unblockUser(Long userId) throws UserException {
        Optional<BlockedUser> blocked = blockedUserRepository.findByUserId(userId);
        if (blocked.isEmpty()) {
            throw new UserException("User is not currently blocked with ID: " + userId);
        }

        Notification notification = new Notification();
        notification.setUserEmail(blocked.get().getUserEmail());
        notification.setMessage("Your account has been unblocked. Welcome back!");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        NotificationDTO notificationDTO = NotificationBuilder.generateDTOFromEntity(notification);
        notificationService.sendNotification(notificationDTO);

        blockedUserRepository.deleteByUserId(userId);
    }

    public boolean isUserBlocked(Long userId) {
        return blockedUserRepository.findByUserId(userId).isPresent();
    }

    public Optional<BlockedUserDTO> findByEmail(String email) {
        return blockedUserRepository.findByUserEmail(email)
                .map(BlockedUserBuilder::generateDTOFromEntity);
    }
}
