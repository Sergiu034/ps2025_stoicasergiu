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

import java.util.ArrayList;
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

    public FriendshipDTO createFriendship(String senderEmail, String receiverEmail) throws UserException {
        User sender = userRepository.findUserByEmail(senderEmail)
                .orElseThrow(() -> new UserException("Sender not found"));
        User receiver = userRepository.findUserByEmail(receiverEmail)
                .orElseThrow(() -> new UserException("Receiver not found"));

        if (sender.equals(receiver)) {
            throw new UserException("You cannot send a friend request to yourself.");
        }

        Optional<Friendship> existingFriendship = friendshipRepository
                .findFriendshipByUser1AndUser2(sender, receiver);

        if (existingFriendship.isPresent()) {
            throw new UserException("Friendship already exists between these users.");
        }

        Friendship friendship = new Friendship();
        friendship.setUser1(sender);
        friendship.setUser2(receiver);
        friendship.setStatus("pending");

        Friendship saved = friendshipRepository.save(friendship);
        return FriendshipBuilder.generateDTOFrimEntity(saved);
    }

    public FriendshipDTO acceptFriendship(String receiverEmail, String senderEmail) throws UserException {
        User receiver = userRepository.findUserByEmail(receiverEmail)
                .orElseThrow(() -> new UserException("Receiver not found"));
        User sender = userRepository.findUserByEmail(senderEmail)
                .orElseThrow(() -> new UserException("Sender not found"));

        Optional<Friendship> friendshipOpt = friendshipRepository
                .findFriendshipByUser1AndUser2(sender, receiver);

        Friendship friendship = friendshipOpt.orElseThrow(() ->
                new UserException("Friendship not found"));

        if (!"pending".equals(friendship.getStatus())) {
            throw new UserException("Only pending friendships can be accepted.");
        }

        friendship.setStatus("accepted");
        Friendship updated = friendshipRepository.save(friendship);
        return FriendshipBuilder.generateDTOFrimEntity(updated);
    }

    public void declineFriendship(String receiverEmail, String senderEmail) throws UserException {
        User receiver = userRepository.findUserByEmail(receiverEmail)
                .orElseThrow(() -> new UserException("Receiver not found"));

        User sender = userRepository.findUserByEmail(senderEmail)
                .orElseThrow(() -> new UserException("Sender not found"));

        Optional<Friendship> friendshipOpt = friendshipRepository
                .findFriendshipByUser1AndUser2(sender, receiver);

        Friendship friendship = friendshipOpt.orElseThrow(() ->
                new UserException("Friendship not found"));

        friendshipRepository.delete(friendship);
    }

    public List<FriendshipDTO> getFriendshipByUser(String email) throws UserException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserException("User not found"));

        List<Friendship> friendships = friendshipRepository.findByUser1OrUser2(user, user);

        return friendships.stream()
                .map(FriendshipBuilder::generateDTOFrimEntity)
                .collect(Collectors.toList());
    }

    public List<String> getFriendEmails(String userEmail) throws UserException {
        List<FriendshipDTO> friendships = getFriendshipByUser(userEmail); // your existing method
        List<String> emails = new ArrayList<>();

        for (FriendshipDTO f : friendships) {
            if (f.getUser1Email().equals(userEmail)) {
                emails.add(f.getUser2Email());
            } else {
                emails.add(f.getUser1Email());
            }
        }

        return emails;
    }
}
