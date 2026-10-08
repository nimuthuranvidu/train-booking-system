package com.trainbooking.config;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Authentication
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // Admin train management
                        .requestMatchers("/api/trains/**")
                        .hasRole("ADMIN")

                        // Admin route management
                        .requestMatchers("/api/routes/**")
                        .hasRole("ADMIN")

                        // Admin - view all bookings
                        .requestMatchers(HttpMethod.GET, "/api/bookings")
                        .hasRole("ADMIN")

                        // Admin - view bookings by user
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/bookings/user/**"
                        )
                        .hasRole("ADMIN")

                        // Customer - create booking
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/bookings"
                        )
                        .authenticated()

                        // Customer - view own bookings
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/bookings/my"
                        )
                        .authenticated()

                        // Booking confirmation/cancellation
                        .requestMatchers("/api/bookings/**")
                        .authenticated()

                        // Other APIs remain accessible for now
                        .requestMatchers("/api/**")
                        .permitAll()

                        .anyRequest()
                        .permitAll()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}