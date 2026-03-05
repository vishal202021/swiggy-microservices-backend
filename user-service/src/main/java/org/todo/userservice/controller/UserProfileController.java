package org.todo.userservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.todo.userservice.dto.ToDTO;
import org.todo.userservice.dto.UpdateProfileRequest;
import org.todo.userservice.dto.UserProfileDto;
import org.todo.userservice.model.UserProfile;
import org.todo.userservice.service.PhotoStorageService;
import org.todo.userservice.service.UserProfileService;

import java.util.Map;

@RestController
@RequestMapping("/users/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final PhotoStorageService photoStorageService;

    public UserProfileController(UserProfileService userProfileService,
                                 PhotoStorageService photoStorageService) {
        this.userProfileService = userProfileService;
        this.photoStorageService = photoStorageService;
    }

    @GetMapping
    public ResponseEntity<UserProfileDto> getProfile(
            @RequestHeader("X-User-Id") String userIdHeader,
            @RequestHeader("X-User-Email") String email
    ) {
        Long userId = Long.parseLong(userIdHeader);

        UserProfile profile =
                userProfileService.getOrCreateByUserId(userId, email);

        return ResponseEntity.ok(ToDTO.toDTO(profile));
    }



    @PutMapping
    public ResponseEntity<UserProfileDto> updateProfile(
            @RequestHeader("X-User-Id") String userIdHeader,
            @RequestHeader("X-User-Email") String email,
            @Valid @RequestBody UpdateProfileRequest req
    ) {
        Long userId = Long.parseLong(userIdHeader);

        UserProfile profile =
                userProfileService.updateProfile(userId, email, req);

        return ResponseEntity.ok(ToDTO.toDTO(profile));
    }

    @PostMapping(value = "/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileDto> uploadPhoto(
            @RequestHeader("X-User-Id") String userIdHeader,
            @RequestHeader("X-User-Email") String email,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        Long userId = Long.parseLong(userIdHeader);

        String url = photoStorageService.store(file, userId);
        UserProfile updated =
                userProfileService.updatePhoto(userId, email, url);

        return ResponseEntity.ok(ToDTO.toDTO(updated));
    }
}
