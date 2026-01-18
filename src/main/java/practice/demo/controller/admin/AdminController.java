package practice.demo.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.admin.*;
import practice.demo.service.admin.AdminAuthService;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody AdminLoginRequest request) {
        return adminAuthService.login(request);
    }

    @PostMapping("/create")
    public ApiResponse createAdmin(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CreateAdminRequest request) {
        return adminAuthService.createAdmin(authorizationHeader, request);
    }

    @PutMapping("/update")
    public ApiResponse updateAdmin(@RequestBody UpdateAdminRequest request) {
        return adminAuthService.updateAdmin(request);
    }

    @PutMapping("/status")
    public ApiResponse changeAdminStatus(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UpdateAdminStatusRequest request) {

        return adminAuthService.changeAdminStatus(authorizationHeader, request);
    }
}
