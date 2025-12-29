package practice.demo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.admin.*;
import practice.demo.service.admin.AdminAuthService;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {

    @Autowired
    private AdminAuthService adminAuthService;

    // 🔐 SUPER ADMIN LOGIN
    @PostMapping("/login")
    public ApiResponse login(@RequestBody AdminLoginRequest request) {
        return adminAuthService.login(request);
    }

    // ➕ CREATE ADMIN (SUPER_ADMIN ONLY)
    @PostMapping("/create")
    public ApiResponse createAdmin(@RequestBody CreateAdminRequest request) {
        return adminAuthService.createAdmin(request);
    }

    // ✏ UPDATE ADMIN (SUPER_ADMIN ONLY)
    @PutMapping("/update")
    public ApiResponse updateAdmin(@RequestBody UpdateAdminRequest request) {
        return adminAuthService.updateAdmin(request);
    }
}
