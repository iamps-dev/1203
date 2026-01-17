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

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
        admin.setActive(true);

        userRepository.save(admin);
        return new ApiResponse(true, "Admin created successfully");
    }

    public ApiResponse updateAdmin(UpdateAdminRequest request) {

        User admin = userRepository.findById(request.getAdminId()).orElse(null);

        if (admin == null || admin.getRole().equalsIgnoreCase("SUPER_ADMIN"))
            return new ApiResponse(false, "Admin not found");

        if (userRepository.existsByEmail(request.getNewEmail())
                && !admin.getEmail().equals(request.getNewEmail()))
            return new ApiResponse(false, "Email already in use");

        admin.setEmail(request.getNewEmail());
        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(admin);

        return new ApiResponse(true, "Admin updated successfully");
    }

    public ApiResponse changeAdminStatus(String token, UpdateAdminStatusRequest request) {

        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        User superAdmin = userRepository.findByEmail(email).orElse(null);

        if (superAdmin == null || !superAdmin.getRole().equalsIgnoreCase("SUPER_ADMIN")) {
            return new ApiResponse(false, "Only Super Admin allowed");
        }

        User admin = userRepository.findById(request.getAdminId()).orElse(null);

        if (admin == null || admin.getRole().equalsIgnoreCase("SUPER_ADMIN")) {
            return new ApiResponse(false, "Invalid admin");
        }

        admin.setActive(request.isActive());
        userRepository.save(admin);

        // ✅ RETURN UPDATED ADMIN DATA
        return new ApiResponse(
                true,
                request.isActive()
                        ? "Admin activated successfully"
                        : "Admin deactivated successfully",
                Map.of(
                        "id", admin.getId(),
                        "isActive", admin.isActive()
                )
        );
    }
}
