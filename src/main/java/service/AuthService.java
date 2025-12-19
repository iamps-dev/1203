package service;

import dto.AuthResponse;
import dto.LoginRequest;
import dto.SignUpRequest;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String signUp(SignUpRequest request) throws Exception {
        if(!request.getPassword().equals(request.getConfirmPassword())) {
            throw new Exception("Passwords do not match!");
        }
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new Exception("Email already registered!");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    public AuthResponse login(LoginRequest request) throws Exception {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("User not found!"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new Exception("Invalid password!");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}
