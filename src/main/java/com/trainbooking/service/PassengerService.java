package com.trainbooking.service;

import com.trainbooking.dto.PassengerRequest;
import com.trainbooking.entity.Booking;
import com.trainbooking.entity.BookingSeat;
import com.trainbooking.entity.Passenger;
import com.trainbooking.entity.Seat;
import com.trainbooking.repository.BookingRepository;
import com.trainbooking.repository.BookingSeatRepository;
import com.trainbooking.repository.PassengerRepository;
import com.trainbooking.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public PassengerService(
            PassengerRepository passengerRepository,
            BookingRepository bookingRepository,
            SeatRepository seatRepository,
            BookingSeatRepository bookingSeatRepository) {

        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    public Passenger addPassenger(PassengerRequest request) {

        Booking booking = bookingRepository.findById(
                request.getBookingId()
        ).orElseThrow(() ->
                new RuntimeException("Booking not found"));

        Seat seat = seatRepository.findById(
                request.getSeatId()
        ).orElseThrow(() ->
                new RuntimeException("Seat not found"));

        // Check whether this seat was selected for this booking
        boolean seatSelectedForBooking =
                bookingSeatRepository
                        .findByBookingId(booking.getId())
                        .stream()
                        .anyMatch(bookingSeat ->
                                bookingSeat.getSeat()
                                        .getId()
                                        .equals(seat.getId())
                        );

        if (!seatSelectedForBooking) {
            throw new RuntimeException(
                    "This seat was not selected for this booking"
            );
        }

        // Prevent assigning the same seat to another passenger
        if (passengerRepository.existsByBookingIdAndSeatId(
                booking.getId(),
                seat.getId())) {

            throw new RuntimeException(
                    "This seat is already assigned to a passenger"
            );
        }

        Passenger passenger = new Passenger(
                request.getName(),
                request.getAge(),
                request.getGender(),
                request.getNic(),
                booking,
                seat
        );

        return passengerRepository.save(passenger);
    }

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public List<Passenger> getPassengersByBooking(Long bookingId) {
        return passengerRepository.findByBookingId(bookingId);
    }
}