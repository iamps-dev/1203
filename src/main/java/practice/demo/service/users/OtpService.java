package practice.demo.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.demo.ApiResponse.ApiResponse;
import practice.demo.entity.OtpVerification;
import practice.demo.entity.User;
import practice.demo.repository.OtpRepository;
import practice.demo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private final int OTP_EXPIRY_MINUTES = 5;
    private final int OTP_MAX_RESEND = 3;
    private final int OTP_RESEND_BLOCK_HOURS = 1;

    // ================= SEND OTP =================
    public ApiResponse sendOtp(String email) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return new ApiResponse(false, "User not found");

        String otp = generateOtp();

        OtpVerification otpEntity = otpRepository.findByUser(user)
                .orElse(new OtpVerification());

        LocalDateTime now = LocalDateTime.now();

        otpEntity.setUser(user);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(now.plusMinutes(OTP_EXPIRY_MINUTES));
        otpEntity.setVerified(false);
        otpEntity.setResendCount(0);
        otpEntity.setResendBlockedUntil(null);

        otpRepository.save(otpEntity);
        emailService.sendOtpEmail(user.getEmail(), otp);

        return new ApiResponse(true, "OTP sent successfully");
    }

    // ================= VERIFY OTP =================
    public ApiResponse verifyOtp(String email, String otp) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return new ApiResponse(false, "User not found");

        OtpVerification otpData = otpRepository.findByUserAndVerifiedFalse(user).orElse(null);
        if (otpData == null) return new ApiResponse(false, "OTP not found or already verified");

        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            return new ApiResponse(false, "OTP expired");
        }

        if (!otpData.getOtp().equals(otp)) {
            return new ApiResponse(false, "Invalid OTP");
        }

        otpData.setVerified(true);
        otpRepository.save(otpData);

        return new ApiResponse(true, "OTP verified successfully");
    }

    // ================= RESEND OTP =================
    public ApiResponse resendOtp(String email) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return new ApiResponse(false, "User not found");

        OtpVerification otpEntity = otpRepository.findByUser(user).orElse(null);
        if (otpEntity == null) return new ApiResponse(false, "OTP not requested yet");
        if (otpEntity.isVerified()) return new ApiResponse(false, "OTP already verified");

        LocalDateTime now = LocalDateTime.now();

        // 🔒 Resend blocked
        if (otpEntity.getResendBlockedUntil() != null && now.isBefore(otpEntity.getResendBlockedUntil())) {
            return new ApiResponse(false, "OTP resend blocked. Try again after " + OTP_RESEND_BLOCK_HOURS + " hour(s)");
        }

        if (otpEntity.getResendCount() >= OTP_MAX_RESEND) {
            otpEntity.setResendBlockedUntil(now.plusHours(OTP_RESEND_BLOCK_HOURS));
            otpRepository.save(otpEntity);
            return new ApiResponse(false, "OTP resend limit reached. Blocked for " + OTP_RESEND_BLOCK_HOURS + " hour(s)");
        }

        // ✅ Generate new OTP
        String newOtp = generateOtp();
        otpEntity.setOtp(newOtp);
        otpEntity.setExpiryTime(now.plusMinutes(OTP_EXPIRY_MINUTES));
        otpEntity.setVerified(false);
        otpEntity.setResendCount(otpEntity.getResendCount() + 1);

        otpRepository.save(otpEntity);
        emailService.sendOtpEmail(user.getEmail(), newOtp);

        return new ApiResponse(true, "OTP resent successfully (" + otpEntity.getResendCount() + "/" + OTP_MAX_RESEND + ")");
    }

    // =================== HELPER ===================
    private String generateOtp() {
        return String.valueOf(1000 + new Random().nextInt(9000)); // 4-digit OTP
    }
}
