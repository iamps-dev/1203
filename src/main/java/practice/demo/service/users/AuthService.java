package practice.demo.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.user.LoginRequest;
import practice.demo.dto.user.LoginResponse; // ✅ updated import
import practice.demo.dto.user.SignUpRequest;
import practice.demo.entity.User;
import practice.demo.repository.UserRepository;
import practice.demo.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final OtpService otpService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ================= SIGNUP =================
    public ApiResponse signUp(SignUpRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword()))
            return new ApiResponse(false, "Passwords do not match");

        if (userRepository.findByEmail(request.getEmail()).isPresent())
            return new ApiResponse(false, "Email already registered");

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        // ✅ Send OTP after signup
        otpService.sendOtp(user.getEmail());

        return new ApiResponse(true, "Signup successful. OTP sent to email", user.getEmail());
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
