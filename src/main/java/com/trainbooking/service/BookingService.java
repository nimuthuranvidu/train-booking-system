package com.trainbooking.service;

import com.trainbooking.entity.Booking;
import com.trainbooking.repository.BookingRepository;
import com.trainbooking.repository.BookingSeatRepository;
import com.trainbooking.repository.PassengerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final PassengerRepository passengerRepository;

    public BookingService(
            BookingRepository bookingRepository,
            BookingSeatRepository bookingSeatRepository,
            PassengerRepository passengerRepository) {

        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.passengerRepository = passengerRepository;
    }

    public Booking addBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking confirmBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        int selectedSeatCount =
                bookingSeatRepository
                        .findByBookingId(bookingId)
                        .size();

        int passengerCount =
                passengerRepository
                        .findByBookingId(bookingId)
                        .size();

        if (selectedSeatCount != passengerCount) {

            throw new RuntimeException(
                    "Number of selected seats must equal number of passengers. "
                            + "Selected seats: " + selectedSeatCount
                            + ", Passengers: " + passengerCount
            );
        }

        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }
}