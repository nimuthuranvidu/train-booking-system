package com.trainbooking.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String email, String otp) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(
                "Train Booking System - Email Verification"
        );

        message.setText(
                "Hello,\n\n"
                        + "Your OTP for the Train Booking System is: "
                        + otp
                        + "\n\n"
                        + "This OTP is valid for 5 minutes.\n\n"
                        + "Please do not share this OTP with anyone.\n\n"
                        + "Thank you."
        );

        mailSender.send(message);
    }
}