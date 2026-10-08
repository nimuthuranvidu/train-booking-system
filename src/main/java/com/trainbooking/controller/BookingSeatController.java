package com.trainbooking.controller;

import com.trainbooking.dto.BookingSeatResponseDTO;
import com.trainbooking.dto.SeatSelectionRequest;
import com.trainbooking.entity.BookingSeat;
import com.trainbooking.service.BookingSeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking-seats")
public class BookingSeatController {

    private final BookingSeatService bookingSeatService;

    public BookingSeatController(
            BookingSeatService bookingSeatService) {

        this.bookingSeatService = bookingSeatService;
    }

    @PostMapping
    public BookingSeatResponseDTO addBookingSeat(
            @RequestParam Long bookingId,
            @RequestParam Long seatId) {

        BookingSeat bookingSeat =
                bookingSeatService.addBookingSeat(
                        bookingId,
                        seatId
                );

        return convertToResponse(bookingSeat);
    }

    @PostMapping("/select")
    public List<BookingSeatResponseDTO> selectSeats(
            @RequestBody SeatSelectionRequest request) {

        return bookingSeatService
                .selectSeats(
                        request.getBookingId(),
                        request.getSeatIds()
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @GetMapping
    public List<BookingSeatResponseDTO> getAllBookingSeats() {

        return bookingSeatService
                .getAllBookingSeats()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @GetMapping("/booking/{bookingId}")
    public List<BookingSeatResponseDTO> getSeatsByBooking(
            @PathVariable Long bookingId) {

        return bookingSeatService
                .getSeatsByBooking(bookingId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private BookingSeatResponseDTO convertToResponse(
            BookingSeat bookingSeat) {

        return new BookingSeatResponseDTO(
                bookingSeat.getId(),
                bookingSeat.getBooking().getId(),
                bookingSeat.getSeat().getId(),
                bookingSeat.getSeat()
                        .getCoach()
                        .getCoachNumber(),
                bookingSeat.getSeat()
                        .getSeatNumber(),
                bookingSeat.getSeat()
                        .getSeatType()
        );
    }
}