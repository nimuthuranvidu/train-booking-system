package com.trainbooking.service;

import java.util.List;

import com.trainbooking.dto.BookingRequest;
import com.trainbooking.entity.Booking;
import com.trainbooking.entity.Schedule;
import com.trainbooking.entity.User;
import com.trainbooking.repository.BookingRepository;
import com.trainbooking.repository.BookingSeatRepository;
import com.trainbooking.repository.PassengerRepository;
import com.trainbooking.repository.ScheduleRepository;
import com.trainbooking.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final PassengerRepository passengerRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public BookingService(
            BookingRepository bookingRepository,
            BookingSeatRepository bookingSeatRepository,
            PassengerRepository passengerRepository,
            UserRepository userRepository,
            ScheduleRepository scheduleRepository) {

        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.passengerRepository = passengerRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public Booking addBooking(BookingRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Schedule schedule = scheduleRepository.findById(
                request.getScheduleId()
        ).orElseThrow(() ->
                new RuntimeException("Schedule not found"));

        if (request.getTravelDate() == null) {
            throw new RuntimeException(
                    "Travel date is required"
            );
        }

        if (!schedule.getOperatingDays().stream()
                .anyMatch(day ->
                        day.getDay()
                                .equals(request.getTravelDate().getDayOfWeek()))) {

            throw new RuntimeException(
                    "This schedule does not operate on the selected date"
            );
        }

        Booking booking = new Booking(
                user,
                schedule,
                request.getTravelDate(),
                "PENDING",
                request.getTotalAmount()
        );

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking confirmBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException(
                    "Cancelled booking cannot be confirmed"
            );
        }

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

    public Booking cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException(
                    "Booking is already cancelled"
            );
        }

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }
}