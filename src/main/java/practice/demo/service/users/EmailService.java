package practice.demo.service.users;

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
        message.setSubject("Your OTP Code – My Safety");

        String text =
                "Dear User,\n\n" +

                        "We received a request to verify your email address for your My Safety account.\n\n" +

                        "🔐 Your One-Time Password (OTP) is:\n\n" +
                        "    " + otp + "\n\n" +

                        "This OTP is valid for the next 5 minutes. Please do not share this code with anyone.\n\n" +

                        "If you did not request this verification, you can safely ignore this email. " +
                        "No changes will be made to your account.\n\n" +

                        "Regards,\n" +
                        "My Safety Team\n\n" +

                        "⚠️ This is an automated message. Please do not reply to this email.";

        message.setText(text);

        message.setFrom("My Safety <iamps.software@gmail.com>");

        mailSender.send(message);
    }
}
