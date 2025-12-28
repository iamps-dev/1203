package practice.demo.service.users;

import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.AuthResponse;
import practice.demo.dto.LoginRequest;
import practice.demo.dto.SignUpRequest;
import practice.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import practice.demo.repository.UserRepository;
import practice.demo.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔹 ADD THIS
    @Autowired
    private OtpService otpService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ================= SIGNUP =================
    // ================= SIGNUP =================
    public ApiResponse signUp(SignUpRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new ApiResponse(false, "Passwords do not match!");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ApiResponse(false, "Email already registered!");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 🔹 Set default role
        user.setRole("USER");

        userRepository.save(user);

        // Send OTP
        otpService.sendOtp(user.getEmail());

        return new ApiResponse(
                true,
                "Signup successful. OTP sent to your email.",
                user.getEmail()   // 👈 data field
        );
    }

    // ================= LOGIN =================
    public ApiResponse login(LoginRequest request) {

        // Check if user exists
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            // Email not registered
            return new ApiResponse(false, "User not found! Please sign up first.");
        }

        // ================= CHECK OTP VERIFICATION =================


        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ApiResponse(false, "Invalid password!");
        }

        // Generate JWT token
        String token = jwtUtil.generateUserToken(user.getEmail());

        return new ApiResponse(true, "Login successful", new AuthResponse(token));
    }
}
