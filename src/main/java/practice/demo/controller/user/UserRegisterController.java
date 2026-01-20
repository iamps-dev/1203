package practice.demo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.user.AddEmergencyContactRequest;
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


    @PostMapping("/emergency-contact/add")
    public ResponseEntity<ApiResponse> addEmergencyContact(
            Authentication authentication,
            @RequestBody AddEmergencyContactRequest request
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(ApiResponse.error("User not authenticated"));
        }

        User user = (User) authentication.getPrincipal();
        ApiResponse response = userRegisterService.addEmergencyContact(user.getId(), request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/emergency-contact/{contactId}/primary-status")
    public ResponseEntity<ApiResponse> changeEmergencyContactPrimaryStatus(
            Authentication authentication,
            @PathVariable Long contactId,
            @RequestParam Boolean isPrimary
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(ApiResponse.error("User not authenticated"));
        }

        if (isPrimary == null) {
            return ResponseEntity.ok(ApiResponse.error("isPrimary value is required"));
        }

        User user = (User) authentication.getPrincipal();

        ApiResponse response =
                userRegisterService.changeEmergencyContactPrimaryStatus(
                        user.getId(),
                        contactId,
                        isPrimary
                );

        return ResponseEntity.ok(response);
    }



}
