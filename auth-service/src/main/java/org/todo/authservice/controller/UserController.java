package org.todo.authservice.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.todo.authservice.model.LoginDTO;
import org.todo.authservice.model.RegisterRequest;
import org.todo.authservice.model.User;
import org.todo.authservice.model.UserResponse;
import org.todo.authservice.service.UserService;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);

        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole().name());

        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestHeader("refresh-token") String refreshToken) {

        String newAccessToken = userService.refreshToken(refreshToken);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token,
                                         @RequestHeader("refresh-token") String refreshToken) {
        userService.logout(token,refreshToken);
        return ResponseEntity.ok("Logged out successfully");
    }
}
