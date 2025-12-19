package controller;


import dto.AuthResponse;
import dto.LoginRequest;
import dto.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public String signUp(@RequestBody SignUpRequest request) throws Exception {
        return authService.signUp(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) throws Exception {
        return authService.login(request);
    }
}
