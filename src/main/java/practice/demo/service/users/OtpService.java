package practice.demo.service.users;

import practice.demo.ApiResponse.ApiResponse;
import practice.demo.entity.OtpVerification;
import practice.demo.entity.User;
import practice.demo.repository.OtpRepository;
import practice.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import practice.demo.service.admin.EmailService;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // ================= SEND OTP =================
    public ApiResponse sendOtp(String email) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return new ApiResponse(false, "User not found");
        }

        // ✅ Generate OTP
        String otp = String.valueOf(1000 + new Random().nextInt(9000));

        OtpVerification otpEntity = otpRepository
                .findByUser(user)
                .orElse(new OtpVerification());

        // ✅ CURRENT TIME = OTP GENERATED TIME
        LocalDateTime now = LocalDateTime.now();

        otpEntity.setUser(user);
        otpEntity.setOtp(otp);

        // ✅ STORE OTP GENERATED TIME

        // ✅ STORE OTP EXPIRY TIME (5 MINUTES)
        otpEntity.setExpiryTime(now.plusMinutes(5));

        otpEntity.setVerified(false);

        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(user.getEmail(), otp);

        return new ApiResponse(true, "OTP sent to email");
    }

    // ================= VERIFY OTP =================
    public ApiResponse verifyOtp(String email, String otp) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return new ApiResponse(false, "User not found");
        }

        OtpVerification otpData =
                otpRepository.findByUserAndVerifiedFalse(user).orElse(null);

        if (otpData == null) {
            return new ApiResponse(false, "OTP not found or already verified");
        }

        // ✅ CHECK EXPIRY USING STORED EXPIRY TIME
        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            return new ApiResponse(false, "OTP expired");
        }

        if (!otpData.getOtp().equals(otp)) {
            return new ApiResponse(false, "Invalid OTP");
        }

        // ✅ Mark OTP verified
        otpData.setVerified(true);
        otpRepository.save(otpData);

        // ❌ DO NOT MARK USER VERIFIED — REMOVE
        // user.setVerified(true);
        // userRepository.save(user);

        return new ApiResponse(true, "OTP verified successfully");
    }

    // ================= RESEND OTP =================
    public ApiResponse resendOtp(String email) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return new ApiResponse(false, "User not found");
        }

        OtpVerification otpEntity = otpRepository
                .findByUser(user)
                .orElse(null);

        if (otpEntity == null) {
            return new ApiResponse(false, "OTP not requested yet");
        }

        // ❌ If OTP already verified
        if (otpEntity.isVerified()) {
            return new ApiResponse(false, "OTP already verified");
        }

        LocalDateTime now = LocalDateTime.now();

        // 🔒 Check if resend is blocked
        if (otpEntity.getResendBlockedUntil() != null) {
            if (now.isBefore(otpEntity.getResendBlockedUntil())) {
                return new ApiResponse(
                        false,
                        "OTP resend blocked. Try again after 1 hour"
                );
            } else {
                // ✅ Auto-unblock after 1 hour
                otpEntity.setResendBlockedUntil(null);
                otpEntity.setResendCount(0);
            }
        }

        // ❌ Max resend reached
        if (otpEntity.getResendCount() >= 3) {
            otpEntity.setResendBlockedUntil(now.plusHours(1));
            otpRepository.save(otpEntity);

            return new ApiResponse(
                    false,
                    "OTP resend limit reached. Blocked for 1 hour"
            );
        }

        // ✅ Generate new OTP
        String newOtp = String.valueOf(1000 + new Random().nextInt(9000));

        otpEntity.setOtp(newOtp);
        otpEntity.setExpiryTime(now.plusMinutes(5));
        otpEntity.setVerified(false);
        otpEntity.setResendCount(otpEntity.getResendCount() + 1);

        otpRepository.save(otpEntity);

        emailService.sendOtpEmail(user.getEmail(), newOtp);

        return new ApiResponse(
                true,
                "OTP resent successfully (" + otpEntity.getResendCount() + "/3)"
        );
    }


}
