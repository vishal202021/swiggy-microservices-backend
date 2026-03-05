package org.todo.userservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @Size(max = 100)
    @NotNull
    private String name;

    @Size(max = 30)
    @NotNull
    private String phone;

    @Size(max = 255)
    @NotNull
    private String address;
}
