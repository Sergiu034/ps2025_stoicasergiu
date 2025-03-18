package com.example.demo.service;


import com.example.demo.builder.friendshipbuilder.FriendshipBuilder;
import com.example.demo.dto.friendshipDTO.FriendshipDTO;
import com.example.demo.dto.userdto.UserViewDTO;
import com.example.demo.entity.Friendship;
import com.example.demo.entity.User;
import com.example.demo.errorhandler.UserException;
import com.example.demo.repository.FriendshipRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public List<FriendshipDTO> findAllFriendships() {

        return friendshipRepository.findAll().stream()
                .map(FriendshipBuilder::generateDTOFrimEntity)
                .collect(Collectors.toList());
    }

    public FriendshipDTO createFriendship(FriendshipDTO friendshipDTO) throws UserException {
        Friendship friendship = FriendshipBuilder.generateEntityFromDTO(friendshipDTO, userRepository);

        Optional<Friendship> existingFriendship = friendshipRepository
                .findFriendshipByUser1AndUser2(friendship.getUser1(), friendship.getUser2());

        if (existingFriendship.isPresent()) {
            throw new UserException("Friendship already exists between these users.");
        }

        Friendship savedFriendship = friendshipRepository.save(friendship);

        return FriendshipBuilder.generateDTOFrimEntity(savedFriendship);
    }

    public FriendshipDTO acceptFriendship(FriendshipDTO friendshipDTO) throws UserException {

        User user1 = userRepository.findUserByEmail(friendshipDTO.getUser1Email())
                .orElseThrow(() -> new UserException("User 1 not found"));

        User user2 = userRepository.findUserByEmail(friendshipDTO.getUser2Email())
                .orElseThrow(() -> new UserException("User 2 not found"));

        Optional<Friendship> friendshipOpt = friendshipRepository
                .findFriendshipByUser1AndUser2(user1, user2);

        if (friendshipOpt.isEmpty()) {
            throw new UserException("Friendship not found between these users.");
        }

        Friendship friendship = friendshipOpt.get();
        if (!"pending".equals(friendship.getStatus())) {
            throw new UserException("This friendship is not pending and cannot be accepted.");
        }

        friendship.setStatus("accepted");

        Friendship updatedFriendship = friendshipRepository.save(friendship);

        return FriendshipBuilder.generateDTOFrimEntity(updatedFriendship);
    }

    public void deleteFriendship(FriendshipDTO friendshipDTO) throws UserException {

        User user1 = userRepository.findUserByEmail(friendshipDTO.getUser1Email())
                .orElseThrow(() -> new UserException("User 1 not found"));

        User user2 = userRepository.findUserByEmail(friendshipDTO.getUser2Email())
                .orElseThrow(() -> new UserException("User 2 not found"));

        Optional<Friendship> friendshipOpt = friendshipRepository
                .findFriendshipByUser1AndUser2(user1, user2);

        if (friendshipOpt.isEmpty()) {
            throw new UserException("Friendship not found between these users.");
        }

        Friendship friendship = friendshipOpt.get();

        this.friendshipRepository.delete(friendship);
    }

    public List<FriendshipDTO> getFriendshipByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            System.out.println("User not found");
        }

        List<Friendship> friendships = friendshipRepository
                .findByUser1OrUser2(user, user);

        return friendships.stream()
                .map(FriendshipBuilder::generateDTOFrimEntity)
                .collect(Collectors.toList());
    }
}
