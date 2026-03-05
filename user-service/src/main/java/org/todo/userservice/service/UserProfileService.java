package org.todo.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.todo.userservice.dto.ToDTO;
import org.todo.userservice.dto.UpdateProfileRequest;
import org.todo.userservice.dto.UserProfileDto;
import org.todo.userservice.model.UserProfile;
import org.todo.userservice.repo.UserProfileRepository;

import java.time.LocalDateTime;

@Service
public class UserProfileService {


    private final UserProfileRepository userProfileRepository;
    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile getOrCreateByUserId(Long userId, String email) {

        UserProfile user = userProfileRepository.findByUserId(userId).orElse(null);

        if (user != null) {
            if (user.getEmail() == null && email != null) {
                user.setEmail(email);
                user.setUpdatedAt(LocalDateTime.now());
                return userProfileRepository.save(user);
            }
            return user;
        }

        UserProfile newUser = new UserProfile();
        newUser.setUserId(userId);
        newUser.setEmail(email);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        return userProfileRepository.save(newUser);
    }


    public UserProfile updateProfile(Long userId, String email, UpdateProfileRequest req) {

        UserProfile user = getOrCreateByUserId(userId, email);

        boolean updated = false;

        if (req.getName() != null) {
            user.setName(req.getName());
            updated = true;
        }

        if (req.getPhone() != null) {
            user.setPhone(req.getPhone());
            updated = true;
        }

        if (req.getAddress() != null) {
            user.setAddress(req.getAddress());
            updated = true;
        }

        if (updated) {
            user.setUpdatedAt(LocalDateTime.now());
            return userProfileRepository.save(user);
        }

        return user;
    }

    public UserProfile updatePhoto(Long userId, String email, String photoUrl) {

        UserProfile user = getOrCreateByUserId(userId, email);

        if (photoUrl != null && !photoUrl.equals(user.getPhotoUrl())) {
            user.setPhotoUrl(photoUrl);
            user.setUpdatedAt(LocalDateTime.now());
            return userProfileRepository.save(user);
        }

        return user;
    }

    public ResponseEntity<UserProfileDto> getUserId(Long userId) {
        UserProfile profile= userProfileRepository.findByUserId(userId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found"));
        UserProfileDto userProfileDto= ToDTO.toDTO(profile);
        return ResponseEntity.ok(userProfileDto);
    }
}
