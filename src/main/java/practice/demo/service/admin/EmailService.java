package practice.demo.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("OTP Verification - My Safety");

        String text = "Hello,\n\n" +
                "Your OTP for My Safety app is: " + otp + "\n\n" +
                "This OTP is valid for 5 minutes only.\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Thank you,\n" +
                "My Safety Team";

        message.setText(text);
        message.setFrom("My Safety <iamps.software@gmail.com>");

        mailSender.send(message);
    }
}
