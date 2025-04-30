package com.example.demo.service.core;

import com.example.demo.entity.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void SendVerificationEmail(User user, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration");
        mailMessage.setText("To confirm your account, please click here: "
                + "http://localhost:8081/api/auth/confirm-account?token=" + token);
        javaMailSender.send(mailMessage);
    }
}
