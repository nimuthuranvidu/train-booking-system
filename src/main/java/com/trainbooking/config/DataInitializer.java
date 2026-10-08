package com.trainbooking.config;

import com.trainbooking.entity.User;
import com.trainbooking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository) {

        return args -> {

            String adminEmail = "admin@trainbooking.com";

            if (userRepository
                    .findByEmail(adminEmail)
                    .isEmpty()) {

                BCryptPasswordEncoder encoder =
                        new BCryptPasswordEncoder();

                User admin = new User();

                admin.setName("System Admin");
                admin.setEmail(adminEmail);
                admin.setPassword(
                        encoder.encode("Admin123")
                );
                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println(
                        "ADMIN account created successfully."
                );

            } else {

                System.out.println(
                        "ADMIN account already exists."
                );
            }
        };
    }
}