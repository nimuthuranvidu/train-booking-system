package com.trainbooking.controller;

import com.trainbooking.dto.OtpVerifyRequest;
import com.trainbooking.dto.RegisterRequest;
import com.trainbooking.entity.PendingRegistration;
import com.trainbooking.entity.User;
import com.trainbooking.repository.PendingRegistrationRepository;
import com.trainbooking.repository.UserRepository;
import com.trainbooking.service.OtpService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.trainbooking.dto.LoginRequest;
import com.trainbooking.dto.ResetPasswordRequest;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OtpService otpService;
    private final UserRepository userRepository;
    private final PendingRegistrationRepository pendingRegistrationRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthController(
            OtpService otpService,
            UserRepository userRepository,
            PendingRegistrationRepository pendingRegistrationRepository) {

        this.otpService = otpService;
        this.userRepository = userRepository;
        this.pendingRegistrationRepository =
                pendingRegistrationRepository;
    }

    @PostMapping("/register")
    public String register(
            @RequestBody RegisterRequest request) {

        if (userRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        if (request.getPassword() == null ||
                request.getPassword().isBlank()) {

            throw new RuntimeException(
                    "Password is required"
            );
        }

        String encodedPassword =
                passwordEncoder.encode(
                        request.getPassword()
                );

        PendingRegistration pendingRegistration =
                new PendingRegistration(
                        request.getName(),
                        request.getEmail(),
                        encodedPassword,
                        LocalDateTime.now().plusMinutes(10)
                );

        pendingRegistrationRepository
                .save(pendingRegistration);

        otpService.generateOtp(
                request.getEmail()
        );

        return "OTP sent successfully to "
                + request.getEmail();
    }

    @PostMapping("/send-otp")
    public String sendOtp(
            @RequestParam String email) {

        otpService.generateOtp(email);

        return "OTP sent successfully to " + email;
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestBody OtpVerifyRequest request) {

        // Verify the OTP
        otpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        // Find pending registration
        PendingRegistration pendingRegistration =
                pendingRegistrationRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No pending registration found"
                                ));

        // Check registration expiry
        if (LocalDateTime.now()
                .isAfter(pendingRegistration.getExpiresAt())) {

            throw new RuntimeException(
                    "Registration has expired"
            );
        }

        // Create new customer
        User user = new User();

        user.setName(
                pendingRegistration.getName()
        );

        user.setEmail(
                pendingRegistration.getEmail()
        );

        user.setRole("CUSTOMER");

        // Save the BCrypt encrypted password
        user.setPassword(
                pendingRegistration.getPassword()
        );

        userRepository.save(user);

        // Remove temporary registration
        pendingRegistrationRepository.delete(
                pendingRegistration
        );

        return "Registration successful. Customer account created.";
    }
    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        ));

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        return "Login successful";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @RequestParam String email) {

        userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No account found with this email"
                        ));

        otpService.generateOtp(email);

        return "OTP sent successfully to " + email;
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestBody ResetPasswordRequest request) {

        // Verify OTP
        otpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        // Validate new password
        if (request.getNewPassword() == null ||
                request.getNewPassword().isBlank()) {

            throw new RuntimeException(
                    "New password is required"
            );
        }

        // Find user
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "No account found with this email"
                        ));

        // Encrypt new password
        String encodedPassword =
                passwordEncoder.encode(
                        request.getNewPassword()
                );

        // Update password
        user.setPassword(encodedPassword);

        userRepository.save(user);

        return "Password reset successful";
    }
}