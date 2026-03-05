package org.todo.userservice.dto;

import org.todo.userservice.model.UserProfile;

public class ToDTO {

    public static UserProfileDto toDTO(UserProfile userProfile) {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setUserId(userProfile.getUserId());
        userProfileDto.setEmail(userProfile.getEmail());
        userProfileDto.setName(userProfile.getName());
        userProfileDto.setPhone(userProfile.getPhone());
        userProfileDto.setAddress(userProfile.getAddress());
        userProfileDto.setPhotoUrl(userProfile.getPhotoUrl());
        return userProfileDto;
    }
}
