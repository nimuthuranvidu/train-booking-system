package com.trainbooking.controller;

import com.trainbooking.config.JwtService;
import com.trainbooking.dto.LoginRequest;
import com.trainbooking.dto.LoginResponseDTO;
import com.trainbooking.dto.OtpVerifyRequest;
import com.trainbooking.dto.RegisterRequest;
import com.trainbooking.dto.ResetPasswordRequest;
import com.trainbooking.entity.PendingRegistration;
import com.trainbooking.entity.User;
import com.trainbooking.repository.PendingRegistrationRepository;
import com.trainbooking.repository.UserRepository;
import com.trainbooking.service.OtpService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OtpService otpService;
    private final UserRepository userRepository;
    private final PendingRegistrationRepository pendingRegistrationRepository;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthController(
            OtpService otpService,
            UserRepository userRepository,
            PendingRegistrationRepository pendingRegistrationRepository,
            JwtService jwtService) {

        this.otpService = otpService;
        this.userRepository = userRepository;
        this.pendingRegistrationRepository =
                pendingRegistrationRepository;
        this.jwtService = jwtService;
    }

    // =========================
    // CUSTOMER REGISTRATION
    // =========================

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

    // =========================
    // SEND OTP
    // =========================

    @PostMapping("/send-otp")
    public String sendOtp(
            @RequestParam String email) {

        otpService.generateOtp(email);

        return "OTP sent successfully to " + email;
    }

    // =========================
    // VERIFY REGISTRATION OTP
    // =========================

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestBody OtpVerifyRequest request) {

        otpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        PendingRegistration pendingRegistration =
                pendingRegistrationRepository
                        .findTopByEmailOrderByIdDesc(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No pending registration found"
                                ));

        if (LocalDateTime.now()
                .isAfter(
                        pendingRegistration.getExpiresAt()
                )) {

            throw new RuntimeException(
                    "Registration has expired"
            );
        }

        User user = new User();

        user.setName(
                pendingRegistration.getName()
        );

        user.setEmail(
                pendingRegistration.getEmail()
        );

        user.setRole("CUSTOMER");

        user.setPassword(
                pendingRegistration.getPassword()
        );

        userRepository.save(user);

        pendingRegistrationRepository.delete(
                pendingRegistration
        );

        return "Registration successful. Customer account created.";
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public LoginResponseDTO login(
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

        // Generate JWT token
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return new LoginResponseDTO(
                "Login successful",
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }


    // =========================
    // ADMIN LOGIN
    // =========================

    @PostMapping("/admin-login")
    public LoginResponseDTO adminLogin(
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

        if (user.getRole() == null ||
                !user.getRole().equalsIgnoreCase("ADMIN")) {

            throw new RuntimeException(
                    "Access denied. Admin account required."
            );
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return new LoginResponseDTO(
                "Admin login successful",
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }


    // =========================
    // FORGOT PASSWORD
    // =========================

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

    // =========================
    // RESET PASSWORD
    // =========================

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestBody ResetPasswordRequest request) {

        otpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        if (request.getNewPassword() == null ||
                request.getNewPassword().isBlank()) {

            throw new RuntimeException(
                    "New password is required"
            );
        }

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "No account found with this email"
                        ));

        String encodedPassword =
                passwordEncoder.encode(
                        request.getNewPassword()
                );

        user.setPassword(encodedPassword);

        userRepository.save(user);

        return "Password reset successful";
    }
}