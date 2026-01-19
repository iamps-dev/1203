package practice.demo.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.admin.*;
import practice.demo.entity.User;
import practice.demo.repository.UserRepository;
import practice.demo.security.JwtUtil;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // =========================
    // LOGIN
    // =========================
    public ApiResponse login(AdminLoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null)
            return new ApiResponse(false, "User not found");

        if (!user.isActive())
            return new ApiResponse(false, "Account deactivated");

        if (!user.getRole().equalsIgnoreCase("ADMIN")
                && !user.getRole().equalsIgnoreCase("SUPER_ADMIN"))
            return new ApiResponse(false, "Access denied");

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return new ApiResponse(false, "Invalid credentials");

        return new ApiResponse(
                true,
                "Login successful",
                jwtUtil.generateAdminToken(user)
        );
    }

    // =========================
    // CREATE ADMIN
    // =========================
    public ApiResponse createAdmin(String token, CreateAdminRequest request) {

        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        User superAdmin = userRepository.findByEmail(email).orElse(null);

        if (superAdmin == null || !superAdmin.getRole().equalsIgnoreCase("SUPER_ADMIN"))
            return new ApiResponse(false, "Only Super Admin can create admins");

        if (userRepository.existsByEmail(request.getEmail()))
            return new ApiResponse(false, "Email already exists");

        User admin = new User();
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole("ADMIN");
        admin.setActive(true); // ✅ explicit

        userRepository.save(admin);
        return new ApiResponse(true, "Admin created successfully");
    }

    // =========================
    // UPDATE ADMIN
    // =========================
    public ApiResponse updateAdmin(UpdateAdminRequest request) {

        User admin = userRepository.findById(request.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (request.getNewEmail() != null) {
            admin.setEmail(request.getNewEmail());
        }

        if (request.getNewPassword() != null) {
            admin.setPassword(request.getNewPassword());
        }

        // 🔥 IMPORTANT FIX
        if (request.getIsActive() != null) {
            admin.setActive(request.getIsActive());
        }

        userRepository.save(admin);

        return ApiResponse.success("Admin updated successfully");
    }

    // =========================
    // CHANGE STATUS
    // =========================
    public ApiResponse changeAdminStatus(String token, UpdateAdminStatusRequest request) {

        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        User superAdmin = userRepository.findByEmail(email).orElse(null);

        if (superAdmin == null || !superAdmin.getRole().equalsIgnoreCase("SUPER_ADMIN"))
            return new ApiResponse(false, "Only Super Admin allowed");

        User admin = userRepository.findById(request.getAdminId()).orElse(null);

        if (admin == null || admin.getRole().equalsIgnoreCase("SUPER_ADMIN"))
            return new ApiResponse(false, "Invalid admin");

        admin.setActive(request.isActive());
        userRepository.save(admin);

        return new ApiResponse(
                true,
                request.isActive()
                        ? "Admin activated successfully"
                        : "Admin deactivated & logged out"
        );
    }
}
