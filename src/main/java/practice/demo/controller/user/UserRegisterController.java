package practice.demo.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.user.RegisterProfileRequest;
import practice.demo.entity.User;
import practice.demo.service.users.UserRegisterService;

@RestController
@RequestMapping("/api/auth/user")  // ✅ class-level mapping same as UserAuthController
@RequiredArgsConstructor
public class UserRegisterController {

    private final UserRegisterService userRegisterService;

    // ✅ REGISTER USER PROFILE (AFTER LOGIN)
    @PostMapping("/register")  // ✅ endpoint path defined here
    public ResponseEntity<ApiResponse> register(
            Authentication authentication,
            @RequestBody RegisterProfileRequest request
    ) throws JsonProcessingException {
        // 🔹 Get logged-in user's email
        User user = (User) authentication.getPrincipal();
        String email = user.getEmail();

        // 🔹 Call service to register profile
        ApiResponse response = userRegisterService.registerProfile(email, request);

        return ResponseEntity.ok(response);
    }
}
