package org.todo.authservice.model;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String role;
}
