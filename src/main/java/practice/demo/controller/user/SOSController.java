package practice.demo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.user.SOSRequest;
import practice.demo.service.users.SOSService;

@RestController
@RequestMapping("/api/auth/user")
@RequiredArgsConstructor
public class SOSController {

    private final SOSService sosService;

    @PostMapping("/sos")
    public ApiResponse sendSOS(
            Authentication authentication,
            @RequestBody SOSRequest request
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ApiResponse.error("User not authenticated");
        }

        // ✅ Get logged-in user ID
        Long userId = ((practice.demo.entity.User) authentication.getPrincipal()).getId();

        // ✅ Call service
        return sosService.sendSOS(userId, request);
    }
}
