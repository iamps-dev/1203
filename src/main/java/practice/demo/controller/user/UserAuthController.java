package practice.demo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.user.ForgotPasswordRequest;
import practice.demo.dto.user.LoginRequest;
import practice.demo.dto.user.SignUpRequest;
import practice.demo.dto.user.OtpVerifyRequest;
import practice.demo.service.users.AuthService;
import practice.demo.service.users.OtpService;
//poonam
@RestController
@RequestMapping("/api/auth/user")
@RequiredArgsConstructor
public class UserAuthController {

    private final AuthService authService;
    private final OtpService otpService;

    // ================= SIGNUP =================
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // ================= OTP SEND/VERIFY/RESEND =================
    @PostMapping("/otp/send")
    public ApiResponse sendOtp(@RequestBody OtpVerifyRequest request) {
        return otpService.sendOtp(request.getEmail());
    }

    @PostMapping("/otp/verify")
    public ApiResponse verifyOtp(@RequestBody OtpVerifyRequest request) {
        return otpService.verifyOtp(request.getEmail(), request.getOtp());
    }

    @PostMapping("/otp/resend")
    public ApiResponse resendOtp(@RequestBody OtpVerifyRequest request) {
        return otpService.resendOtp(request.getEmail());
    }

    // ================= FORGOT PASSWORD (SINGLE API) =================
    @PostMapping("/forgot-password")
    public ApiResponse forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return otpService.forgotPasswordSingleFlow(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword(),
                request.getConfirmPassword()
        );
    }


}
