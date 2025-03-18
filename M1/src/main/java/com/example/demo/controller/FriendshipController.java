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

@RestController
@CrossOrigin
@RequestMapping(value="/api/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @RequestMapping(value="/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crateFriendship(@RequestBody FriendshipDTO friendshipDTO) throws UserException {
        FriendshipDTO createdFriendship = friendshipService.createFriendship(friendshipDTO);
        return new ResponseEntity<>(createdFriendship, HttpStatus.CREATED);
    }

    @RequestMapping(value="/accept", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> acceptFriendship(@RequestBody FriendshipDTO friendshipDTO) throws UserException {
        FriendshipDTO acceptedFriendship = friendshipService.acceptFriendship(friendshipDTO);
        return new ResponseEntity<>(acceptedFriendship, HttpStatus.OK);
    }

    @RequestMapping(value="/decline", method = RequestMethod.DELETE)
    public ResponseEntity<?> declineFriendship(@RequestBody FriendshipDTO friendshipDTO) throws UserException {
        friendshipService.deleteFriendship(friendshipDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/getAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAllFriendships() throws UserException {
        return new ResponseEntity<>(friendshipService.findAllFriendships(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getByUser/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getFriendshipByUser(@PathVariable Long userId) throws UserException {
        return new ResponseEntity<>(friendshipService.getFriendshipByUser(userId), HttpStatus.OK);
    }




}
