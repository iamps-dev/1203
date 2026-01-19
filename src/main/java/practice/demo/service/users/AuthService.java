package practice.demo.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.user.LoginRequest;
import practice.demo.dto.user.LoginResponse; // ✅ updated import
import practice.demo.dto.user.SignUpRequest;
import practice.demo.entity.OtpVerification;
import practice.demo.entity.User;
import practice.demo.repository.UserRepository;
import practice.demo.security.JwtUtil;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ================= SIGNUP =================
    public ApiResponse signUp(SignUpRequest request) {

        // 1️⃣ Password match check
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new ApiResponse(false, "Passwords do not match");
        }

        // 2️⃣ Email format validation (extra safety)
        if (request.getEmail() == null ||
                !request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new ApiResponse(false, "Invalid email address");
        }

        // 3️⃣ Email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ApiResponse(false, "Email already registered");
        }

        // 4️⃣ Save user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        // 5️⃣ Send OTP & CHECK RESULT
        ApiResponse otpResponse = otpService.sendOtp(user.getEmail());

        if (!otpResponse.isSuccess()) {
            return new ApiResponse(false, otpResponse.getMessage());
        }

        // ✅ FINAL SUCCESS
        return new ApiResponse(true, "Signup successful. OTP sent to email");
    }

    // ================= LOGIN =================
    public ApiResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) return new ApiResponse(false, "User not found. Please signup first");

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return new ApiResponse(false, "Invalid password");

        // ✅ Generate JWT token
        String token = jwtUtil.generateUserToken(user.getEmail());

        return new ApiResponse(true, "Login successful", new LoginResponse(token)); // ✅ updated
    }



}
