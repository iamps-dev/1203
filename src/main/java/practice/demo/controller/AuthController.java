package practice.demo.controller;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.LoginRequest;
import practice.demo.dto.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.demo.service.AuthService;

@RestController
@RequestMapping("/api/auth/user")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(
            @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(



            @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
