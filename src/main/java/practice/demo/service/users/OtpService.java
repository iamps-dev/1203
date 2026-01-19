package practice.demo.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // =================== HELPER ===================
    private String generateOtp() {
        return String.valueOf(1000 + new Random().nextInt(9000)); // 4-digit OTP
    }

    // =================== EXISTING OTP METHODS ===================
    public ApiResponse sendOtp(String email) {

        // 1️⃣ Email format validation (VERY IMPORTANT)
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new ApiResponse(false, "Invalid email address");
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return new ApiResponse(false, "User not found");
        }

        try {
            String otp = generateOtp();
            LocalDateTime now = LocalDateTime.now();

            OtpVerification otpEntity =
                    otpRepository.findByUser(user).orElse(new OtpVerification());

            otpEntity.setUser(user);
            otpEntity.setOtp(otp);
            otpEntity.setExpiryTime(now.plusMinutes(OTP_EXPIRY_MINUTES));
            otpEntity.setVerified(false);
            otpEntity.setResendCount(0);
            otpEntity.setResendBlockedUntil(null);

            otpRepository.save(otpEntity);

            // 2️⃣ EMAIL SEND (protected)
            emailService.sendOtpEmail(user.getEmail(), otp);

            return new ApiResponse(true, "OTP sent successfully");

        } catch (Exception e) {
            // ❌ No big stacktrace for client
            return new ApiResponse(false, "Failed to send OTP. Please check email address");
        }
    }

    public ApiResponse verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return new ApiResponse(false, "User not found");

        OtpVerification otpData = otpRepository.findByUserAndVerifiedFalse(user).orElse(null);
        if (otpData == null) return new ApiResponse(false, "OTP not found or already verified");

        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) return new ApiResponse(false, "OTP expired");

        if (!otpData.getOtp().equals(otp)) return new ApiResponse(false, "Invalid OTP");

        otpData.setVerified(true);
        otpRepository.save(otpData);

        return new ApiResponse(true, "OTP verified successfully");
    }

    public ApiResponse resendOtp(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return new ApiResponse(false, "User not found");

        OtpVerification otpEntity = otpRepository.findByUser(user).orElse(null);
        if (otpEntity == null) return new ApiResponse(false, "OTP not requested yet");
        if (otpEntity.isVerified()) return new ApiResponse(false, "OTP already verified");

        LocalDateTime now = LocalDateTime.now();

        if (otpEntity.getResendBlockedUntil() != null && now.isBefore(otpEntity.getResendBlockedUntil())) {
            return new ApiResponse(false, "OTP resend blocked. Try again after " + OTP_RESEND_BLOCK_HOURS + " hour(s)");
        }

        if (otpEntity.getResendCount() >= OTP_MAX_RESEND) {
            otpEntity.setResendBlockedUntil(now.plusHours(OTP_RESEND_BLOCK_HOURS));
            otpRepository.save(otpEntity);
            return new ApiResponse(false, "OTP resend limit reached. Blocked for " + OTP_RESEND_BLOCK_HOURS + " hour(s)");
        }

        String newOtp = generateOtp();
        otpEntity.setOtp(newOtp);
        otpEntity.setExpiryTime(now.plusMinutes(OTP_EXPIRY_MINUTES));
        otpEntity.setVerified(false);
        otpEntity.setResendCount(otpEntity.getResendCount() + 1);

        otpRepository.save(otpEntity);
        emailService.sendOtpEmail(user.getEmail(), newOtp);

        return new ApiResponse(true, "OTP resent successfully (" + otpEntity.getResendCount() + "/" + OTP_MAX_RESEND + ")");
    }

    public ApiResponse forgotPasswordSingleFlow(
            String email,
            String otp,
            String newPassword,
            String confirmPassword
    ) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return new ApiResponse(false, "User not found");
        }

        LocalDateTime now = LocalDateTime.now();
        OtpVerification otpData = otpRepository.findByUser(user).orElse(null);

        // ================= STEP 1: SEND OTP (ALWAYS NEW) =================
        if (otp == null || otp.isBlank()) {

            String generatedOtp = generateOtp();

            if (otpData == null) {
                otpData = new OtpVerification();
                otpData.setUser(user);
            }

            otpData.setOtp(generatedOtp);
            otpData.setExpiryTime(now.plusMinutes(OTP_EXPIRY_MINUTES));
            otpData.setVerified(false);

            otpRepository.save(otpData);
            emailService.sendOtpEmail(user.getEmail(), generatedOtp);

            return new ApiResponse(true, "OTP sent to your email");
        }

        // ================= STEP 2: VERIFY OTP =================
        if (otpData == null) {
            return new ApiResponse(false, "OTP not requested");
        }

        if (now.isAfter(otpData.getExpiryTime())) {
            otpRepository.delete(otpData);
            return new ApiResponse(false, "OTP expired. Please request again");
        }

        if (!otpData.getOtp().equals(otp)) {
            return new ApiResponse(false, "Invalid OTP");
        }

        // ================= STEP 3: RESET PASSWORD =================
        if (newPassword == null || confirmPassword == null) {
            return new ApiResponse(true, "OTP verified. Proceed to reset password");
        }

        if (!newPassword.equals(confirmPassword)) {
            return new ApiResponse(false, "Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpRepository.delete(otpData); // 🔥 remove OTP after success

        return new ApiResponse(true, "Password reset successfully");
    }


}
