package com.trainbooking.service;

import com.trainbooking.entity.OtpVerification;
import com.trainbooking.repository.OtpVerificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private final OtpVerificationRepository otpRepository;
    private final EmailService emailService;

    public OtpService(
            OtpVerificationRepository otpRepository,
            EmailService emailService) {

        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    public String generateOtp(String email) {

        Random random = new Random();

        String otp = String.format(
                "%06d",
                random.nextInt(1000000)
        );

        LocalDateTime expiresAt =
                LocalDateTime.now().plusMinutes(5);

        OtpVerification otpVerification =
                new OtpVerification(
                        email,
                        otp,
                        expiresAt
                );

        otpRepository.save(otpVerification);

        emailService.sendOtpEmail(
                email,
                otp
        );

        return otp;
    }

    public boolean verifyOtp(
            String email,
            String otp) {

        OtpVerification otpVerification =
                otpRepository
                        .findTopByEmailOrderByIdDesc(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No OTP found for this email"
                                ));

        if (otpVerification.isVerified()) {
            throw new RuntimeException(
                    "OTP has already been used"
            );
        }

        if (LocalDateTime.now()
                .isAfter(otpVerification.getExpiresAt())) {

            throw new RuntimeException(
                    "OTP has expired"
            );
        }

        if (!otpVerification.getOtp().equals(otp)) {

            throw new RuntimeException(
                    "Invalid OTP"
            );
        }

        otpVerification.setVerified(true);

        otpRepository.save(otpVerification);

        return true;
    }
}