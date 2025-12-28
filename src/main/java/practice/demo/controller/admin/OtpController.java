package practice.demo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.dto.OtpVerifyRequest;
import practice.demo.service.users.OtpService;

@RestController
@RequestMapping("/api/auth/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    // 🔹 Send OTP (JSON)
    @PostMapping("/send")
    public ApiResponse sendOtp(@RequestBody OtpVerifyRequest request) {
        return otpService.sendOtp(request.getEmail());
    }

    // 🔹 Verify OTP (JSON)
    @PostMapping("/verify")
    public ApiResponse verifyOtp(@RequestBody OtpVerifyRequest request) {
        return otpService.verifyOtp(request.getEmail(), request.getOtp());
    }
}
