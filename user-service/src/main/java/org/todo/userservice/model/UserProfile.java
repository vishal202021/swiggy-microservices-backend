package org.todo.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    @Column(nullable = false, unique = true)
    private Long userId;
    @Column(nullable = false)
    private String email;
    private String phone;
    private String photoUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
