package com.trainbooking.service;

import com.trainbooking.entity.Booking;
import com.trainbooking.entity.Schedule;
import com.trainbooking.entity.User;
import com.trainbooking.repository.BookingRepository;
import com.trainbooking.repository.ScheduleRepository;
import com.trainbooking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            ScheduleRepository scheduleRepository) {

        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public Booking createBooking(
            Long userId,
            Long scheduleId,
            LocalDate travelDate,
            double totalAmount) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Schedule schedule = scheduleRepository
                .findById(scheduleId)
                .orElseThrow(() ->
                        new RuntimeException("Schedule not found"));

        if (travelDate == null) {
            throw new RuntimeException(
                    "Travel date is required");
        }

        if (travelDate.isBefore(LocalDate.now())) {
            throw new RuntimeException(
                    "Travel date cannot be in the past");
        }

        if (totalAmount <= 0) {
            throw new RuntimeException(
                    "Total amount must be greater than 0");
        }

        DayOfWeek travelDay =
                travelDate.getDayOfWeek();

        boolean operating =
                schedule.getOperatingDays()
                        .stream()
                        .anyMatch(day ->
                                day.getDay() == travelDay);

        if (!operating) {
            throw new RuntimeException(
                    "Train does not operate on " + travelDay);
        }

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setSchedule(schedule);
        booking.setTravelDate(travelDate);
        booking.setStatus("PENDING");
        booking.setTotalAmount(totalAmount);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        return bookingRepository.findByUserId(userId);
    }

    public Booking confirmBooking(Long bookingId) {

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found"));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException(
                    "Cancelled booking cannot be confirmed");
        }

        if (!"PENDING".equals(booking.getStatus())) {
            throw new RuntimeException(
                    "Only pending bookings can be confirmed");
        }

        int passengerCount =
                booking.getPassengers() == null
                        ? 0
                        : booking.getPassengers().size();

        if (passengerCount == 0) {
            throw new RuntimeException(
                    "At least one passenger is required");
        }

        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long bookingId) {

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found"));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException(
                    "Booking is already cancelled");
        }

        if (!"PENDING".equals(booking.getStatus()) &&
                !"CONFIRMED".equals(booking.getStatus())) {

            throw new RuntimeException(
                    "Only pending or confirmed bookings can be cancelled");
        }

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }
}