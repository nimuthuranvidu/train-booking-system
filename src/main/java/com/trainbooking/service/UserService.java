
package com.trainbooking.service;

import com.trainbooking.entity.User;
import com.trainbooking.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (user.getPassword() == null ||
                user.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Public registration can only create CUSTOMER accounts
        user.setRole("CUSTOMER");

        return userRepository.save(user);
    }
}
