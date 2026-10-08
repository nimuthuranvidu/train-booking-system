package com.trainbooking.controller;

import com.trainbooking.dto.BookingRequest;
import com.trainbooking.dto.BookingResponseDTO;
import com.trainbooking.dto.UserResponseDTO;
import com.trainbooking.entity.Booking;
import com.trainbooking.entity.User;
import com.trainbooking.repository.UserRepository;
import com.trainbooking.service.BookingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingController(
            BookingService bookingService,
            UserRepository userRepository) {

        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    // CUSTOMER - Create booking
    @PostMapping
    public BookingResponseDTO createBooking(
            @RequestBody BookingRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Booking savedBooking =
                bookingService.createBooking(
                        user.getId(),
                        request.getScheduleId(),
                        request.getTravelDate(),
                        request.getTotalAmount()
                );

        return convertToDTO(savedBooking);
    }

    // ADMIN - View all bookings
    @GetMapping
    public List<BookingResponseDTO> getAllBookings() {

        return bookingService
                .getAllBookings()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // CUSTOMER - View own bookings
    @GetMapping("/my")
    public List<BookingResponseDTO> getMyBookings(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return bookingService
                .getBookingsByUserId(user.getId())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ADMIN - View bookings of a specific user
    @GetMapping("/user/{userId}")
    public List<BookingResponseDTO> getBookingsByUser(
            @PathVariable Long userId) {

        return bookingService
                .getBookingsByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Confirm booking
    @PutMapping("/{bookingId}/confirm")
    public BookingResponseDTO confirmBooking(
            @PathVariable Long bookingId) {

        Booking booking =
                bookingService.confirmBooking(bookingId);

        return convertToDTO(booking);
    }

    // Cancel booking
    @PutMapping("/{bookingId}/cancel")
    public BookingResponseDTO cancelBooking(
            @PathVariable Long bookingId) {

        Booking booking =
                bookingService.cancelBooking(bookingId);

        return convertToDTO(booking);
    }

    private BookingResponseDTO convertToDTO(
            Booking booking) {

        UserResponseDTO userDTO =
                new UserResponseDTO(
                        booking.getUser().getId(),
                        booking.getUser().getName(),
                        booking.getUser().getEmail(),
                        booking.getUser().getRole()
                );

        return new BookingResponseDTO(
                booking.getId(),
                userDTO,
                booking.getSchedule().getId(),
                booking.getTravelDate(),
                booking.getStatus(),
                booking.getTotalAmount()
        );
    }
}