package org.todo.notificationservice.dto;

import lombok.Data;

@Data
public class UserContactDto {
    private Long userId;
    private String email;
    private String name;
    private String phone;
    private String address;
    private String photoUrl;
}
