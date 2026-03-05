package org.todo.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.todo.userservice.dto.UserProfileDto;
import org.todo.userservice.service.UserProfileService;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserProfileService userService;
    private static final Logger log = LoggerFactory.getLogger(InternalUserController.class);
    {
        log.info("InternalUserController LOADED");
    }
    @GetMapping("/{userId}/contact")
    public ResponseEntity<UserProfileDto> getUserContact(@PathVariable Long userId) {
        log.info("getUserContact HIT for userId={}", userId);
        return userService.getUserId(userId);
    }
}
