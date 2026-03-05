package org.todo.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.todo.authservice.jwt.JwtUtil;
import org.todo.authservice.model.LoginDTO;
import org.todo.authservice.model.RegisterRequest;
import org.todo.authservice.model.Role;
import org.todo.authservice.model.User;
import org.todo.authservice.repo.UserRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final Set<String> blacklistedTokens = new HashSet<>();

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.valueOf(req.getRole().toUpperCase()));

        return userRepository.save(user);
    }

    public Map<String, String> login(LoginDTO loginDTO) {

        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "role", user.getRole().name()
        );
    }

    public String refreshToken(String refreshToken) {

        if (isTokenBlacklisted(refreshToken)) {
            throw new RuntimeException("Token has been logged out");
        }

        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token expired!");
        }

        String email = jwtUtil.extractEmail(refreshToken);

        User user=userRepository.findByEmail(email).orElseThrow(()
                -> new RuntimeException("User not found"));
        return jwtUtil.generateAccessToken(user);
    }

    public void logout(String token,String refreshToken) {
        token = token.replace("Bearer ", "");
        refreshToken = refreshToken.replace("Bearer ", "");
        blacklistedTokens.add(token);
        blacklistedTokens.add(refreshToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
