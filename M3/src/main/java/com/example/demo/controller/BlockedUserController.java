package com.example.demo.controller;

import com.example.demo.dto.blockeduserdto.BlockedUserDTO;
import com.example.demo.errorhandler.UserException;
import com.example.demo.service.BlockedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/moderation")
@RequiredArgsConstructor
public class BlockedUserController {

    private final BlockedUserService blockedUserService;

    @RequestMapping(method = RequestMethod.POST, value = "/block")
    public ResponseEntity<?> processBlockUser(@RequestBody BlockedUserDTO dto) throws UserException {
        return new ResponseEntity<>(blockedUserService.blockUser(dto), HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.DELETE, value = "/unblock/{userId}")
    public ResponseEntity<?> processUnblockUser(@PathVariable("userId") Long userId) throws UserException {
        blockedUserService.unblockUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/is-blocked/{userId}")
    public ResponseEntity<?> checkIfUserIsBlocked(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(blockedUserService.isUserBlocked(userId), HttpStatus.OK);
    }
}
