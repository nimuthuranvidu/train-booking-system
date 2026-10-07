package com.trainbooking.service;

import com.trainbooking.entity.Booking;
import com.trainbooking.entity.BookingSeat;
import com.trainbooking.entity.Seat;
import com.trainbooking.repository.BookingRepository;
import com.trainbooking.repository.BookingSeatRepository;
import com.trainbooking.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookingSeatService {

    private final BookingSeatRepository bookingSeatRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    public BookingSeatService(
            BookingSeatRepository bookingSeatRepository,
            BookingRepository bookingRepository,
            SeatRepository seatRepository) {

        this.bookingSeatRepository = bookingSeatRepository;
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
    }

    public BookingSeat addBookingSeat(Long bookingId, Long seatId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() ->
                        new RuntimeException("Seat not found"));

        if (bookingSeatRepository.existsBySeatIdAndTravelDate(
                seatId,
                booking.getTravelDate()
        )) {
            throw new RuntimeException(
                    "Seat is already booked for this travel date"
            );
        }

        BookingSeat bookingSeat =
                new BookingSeat(booking, seat);

        return bookingSeatRepository.save(bookingSeat);
    }

    public List<BookingSeat> selectSeats(
            Long bookingId,
            List<Long> seatIds) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if (seatIds == null || seatIds.isEmpty()) {
            throw new RuntimeException(
                    "At least one seat must be selected"
            );
        }

        // Prevent duplicate seat IDs in the same request
        Set<Long> uniqueSeatIds = new HashSet<>(seatIds);

        if (uniqueSeatIds.size() != seatIds.size()) {
            throw new RuntimeException(
                    "Duplicate seat selected"
            );
        }

        List<BookingSeat> selectedSeats = new java.util.ArrayList<>();

        for (Long seatId : seatIds) {

            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Seat not found: " + seatId
                            ));

            boolean alreadyBooked =
                    bookingSeatRepository
                            .existsBySeatIdAndTravelDate(
                                    seatId,
                                    booking.getTravelDate()
                            );

            if (alreadyBooked) {
                throw new RuntimeException(
                        "Seat " + seat.getSeatNumber()
                                + " in "
                                + seat.getCoach().getCoachNumber()
                                + " is already booked"
                );
            }

            BookingSeat bookingSeat =
                    new BookingSeat(booking, seat);

            selectedSeats.add(bookingSeat);
        }

        return bookingSeatRepository.saveAll(selectedSeats);
    }

    public List<BookingSeat> getSeatsByBooking(Long bookingId) {
        return bookingSeatRepository.findByBookingId(bookingId);
    }

    public List<BookingSeat> getAllBookingSeats() {
        return bookingSeatRepository.findAll();
    }
}