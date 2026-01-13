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
        User user = (User) authentication.getPrincipal();
        String email = user.getEmail();

        ApiResponse response =
                userRegisterService.registerProfile(email, request);

        return ResponseEntity.ok(response);
    }
}
