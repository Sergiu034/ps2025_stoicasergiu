package com.example.demo.controller;


import com.example.demo.dto.friendshipDTO.FriendshipDTO;
import com.example.demo.dto.userdto.UserDTO;
import com.example.demo.errorhandler.UserException;
import com.example.demo.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value="/api/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFriendship(@RequestBody FriendshipDTO friendshipDTO, Principal principal) throws UserException {
        String senderEmail = principal.getName();
        FriendshipDTO createdFriendship = friendshipService.createFriendship(senderEmail, friendshipDTO.getUser2Email());
        return new ResponseEntity<>(createdFriendship, HttpStatus.CREATED);
    }

    @PutMapping(value = "/accept", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> acceptFriendship(@RequestBody FriendshipDTO friendshipDTO, Principal principal) throws UserException {
        String receiverEmail = principal.getName();
        FriendshipDTO acceptedFriendship = friendshipService.acceptFriendship(receiverEmail, friendshipDTO.getUser1Email());
        return new ResponseEntity<>(acceptedFriendship, HttpStatus.OK);
    }


    @PutMapping(value = "/decline", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> declineFriendship(@RequestBody FriendshipDTO friendshipDTO, Principal principal) throws UserException {
        String receiverEmail = principal.getName();
        friendshipService.declineFriendship(receiverEmail, friendshipDTO.getUser1Email());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllFriendships() {
        return new ResponseEntity<>(friendshipService.findAllFriendships(), HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyFriendships(Principal principal) throws UserException {
        String userEmail = principal.getName();
        return new ResponseEntity<>(friendshipService.getFriendshipByUser(userEmail), HttpStatus.OK);
    }

    @GetMapping("/my/emails")
    public ResponseEntity<List<String>> getMyFriendEmails(Principal principal) throws UserException {
        String userEmail = principal.getName();
        return new ResponseEntity<>(friendshipService.getFriendEmails(userEmail), HttpStatus.OK);
    }

}
