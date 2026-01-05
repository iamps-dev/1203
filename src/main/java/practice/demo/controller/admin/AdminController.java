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
