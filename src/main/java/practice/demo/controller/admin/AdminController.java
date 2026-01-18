package practice.demo.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.admin.*;
import practice.demo.dto.admin.allAdminResponse;
import practice.demo.service.admin.AdminAuthService;
import practice.demo.service.admin.AdminListService;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminAuthService adminAuthService;
    private final AdminListService adminListService;

    // =========================
    // 🔐 Admin Login
    // =========================
    @PostMapping("/login")
    public ApiResponse login(@RequestBody AdminLoginRequest request) {
        return adminAuthService.login(request);
    }

    // =========================
    // ➕ Create Admin (SUPER_ADMIN)
    // =========================
    @PostMapping("/create")
    public ApiResponse createAdmin(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CreateAdminRequest request) {

        return adminAuthService.createAdmin(authorizationHeader, request);
    }

    // =========================
    // ✏️ Update Admin
    // =========================
    @PutMapping("/update")
    public ApiResponse updateAdmin(@RequestBody UpdateAdminRequest request) {
        return adminAuthService.updateAdmin(request);
    }

    // =========================
    // 🔁 Change Admin Status
    // =========================
    @PutMapping("/status")
    public ApiResponse changeAdminStatus(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UpdateAdminStatusRequest request) {

        return adminAuthService.changeAdminStatus(authorizationHeader, request);
    }

    // =========================
    // 📋 Get All Admins (SUPER_ADMIN)
    // =========================
    @GetMapping("/all")
    public allAdminResponse getAllAdmins(
            @RequestHeader("Authorization") String authorizationHeader) {

        return adminListService.getAllAdmins(authorizationHeader);
    }
}
