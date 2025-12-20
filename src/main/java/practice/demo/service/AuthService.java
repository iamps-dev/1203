package practice.demo.service;

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

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        userRepository.save(user);

        return new ApiResponse(true, "User registered successfully!");
    }

    public ApiResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return new ApiResponse(false, "User not found!");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ApiResponse(false, "Invalid password!");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new ApiResponse(
                true,
                "Login successful",
                new AuthResponse(token)
        );
    }
}
