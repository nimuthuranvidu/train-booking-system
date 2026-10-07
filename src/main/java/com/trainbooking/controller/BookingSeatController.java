package com.trainbooking.controller;

import com.trainbooking.dto.SeatSelectionRequest;
import com.trainbooking.entity.BookingSeat;
import com.trainbooking.service.BookingSeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking-seats")
public class BookingSeatController {

    private final BookingSeatService bookingSeatService;

    public BookingSeatController(BookingSeatService bookingSeatService) {
        this.bookingSeatService = bookingSeatService;
    }

    @PostMapping
    public BookingSeat addBookingSeat(
            @RequestParam Long bookingId,
            @RequestParam Long seatId) {

        return bookingSeatService.addBookingSeat(
                bookingId,
                seatId
        );
    }

    @PostMapping("/select")
    public List<BookingSeat> selectSeats(
            @RequestBody SeatSelectionRequest request) {

        return bookingSeatService.selectSeats(
                request.getBookingId(),
                request.getSeatIds()
        );
    }

    @GetMapping
    public List<BookingSeat> getAllBookingSeats() {
        return bookingSeatService.getAllBookingSeats();
    }

    @GetMapping("/booking/{bookingId}")
    public List<BookingSeat> getSeatsByBooking(
            @PathVariable Long bookingId) {

        return bookingSeatService.getSeatsByBooking(bookingId);
    }
}