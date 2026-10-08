package com.trainbooking.controller;

import com.trainbooking.dto.BookingRequest;
import com.trainbooking.dto.BookingResponseDTO;
import com.trainbooking.dto.UserResponseDTO;
import com.trainbooking.entity.Booking;
import com.trainbooking.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseDTO addBooking(
            @RequestBody BookingRequest request) {

        Booking savedBooking =
                bookingService.addBooking(request);

        return convertToResponse(savedBooking);
    }

    @GetMapping
    public List<BookingResponseDTO> getAllBookings() {

        return bookingService.getAllBookings()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @PutMapping("/{bookingId}/confirm")
    public BookingResponseDTO confirmBooking(
            @PathVariable Long bookingId) {

        Booking confirmedBooking =
                bookingService.confirmBooking(bookingId);

        return convertToResponse(confirmedBooking);
    }

    @PutMapping("/{bookingId}/cancel")
    public BookingResponseDTO cancelBooking(
            @PathVariable Long bookingId) {

        Booking cancelledBooking =
                bookingService.cancelBooking(bookingId);

        return convertToResponse(cancelledBooking);
    }

    private BookingResponseDTO convertToResponse(
            Booking booking) {

        UserResponseDTO user =
                new UserResponseDTO(
                        booking.getUser().getId(),
                        booking.getUser().getName(),
                        booking.getUser().getEmail(),
                        booking.getUser().getRole()
                );

        return new BookingResponseDTO(
                booking.getId(),
                user,
                booking.getSchedule().getId(),
                booking.getTravelDate(),
                booking.getStatus(),
                booking.getTotalAmount()
        );
    }
}