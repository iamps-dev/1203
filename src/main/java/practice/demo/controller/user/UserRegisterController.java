package practice.demo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.user.RegisterProfileRequest;
import practice.demo.entity.User;
import practice.demo.service.users.UserRegisterService;

@RestController
@RequestMapping("/api/auth/user")
@RequiredArgsConstructor
public class UserRegisterController {

    private final UserRegisterService userRegisterService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            Authentication authentication,
            @RequestBody RegisterProfileRequest request
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(ApiResponse.error("User not authenticated"));
        }

        User user = (User) authentication.getPrincipal(); // ✅ SAME SOURCE
        ApiResponse response =
                userRegisterService.registerProfile(user.getId(), request);

        return ResponseEntity.ok(response);
    }
}
