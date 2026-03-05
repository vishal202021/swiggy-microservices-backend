package org.todo.userservice.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private Long userId;
    private String email;
    private String name;
    private String phone;
    private String address;
    private String photoUrl;
}
