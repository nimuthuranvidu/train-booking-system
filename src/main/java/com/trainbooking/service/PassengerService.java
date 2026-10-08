package com.trainbooking.service;

import com.trainbooking.dto.PassengerRequest;
import com.trainbooking.entity.Booking;
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

    // ADD passenger
    public Passenger addPassenger(PassengerRequest request) {

        Booking booking = bookingRepository.findById(
                request.getBookingId()
        ).orElseThrow(() ->
                new RuntimeException("Booking not found"));

        Seat seat = seatRepository.findById(
                request.getSeatId()
        ).orElseThrow(() ->
                new RuntimeException("Seat not found"));

        validateSeatSelectedForBooking(booking, seat);

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

    // VIEW all passengers
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    // VIEW passengers by booking
    public List<Passenger> getPassengersByBooking(Long bookingId) {

        if (!bookingRepository.existsById(bookingId)) {
            throw new RuntimeException("Booking not found");
        }

        return passengerRepository.findByBookingId(bookingId);
    }

    // UPDATE passenger
    public Passenger updatePassenger(
            Long id,
            PassengerRequest request) {

        Passenger existingPassenger =
                passengerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Passenger not found"));

        Booking booking = bookingRepository.findById(
                request.getBookingId()
        ).orElseThrow(() ->
                new RuntimeException("Booking not found"));

        Seat seat = seatRepository.findById(
                request.getSeatId()
        ).orElseThrow(() ->
                new RuntimeException("Seat not found"));

        validateSeatSelectedForBooking(booking, seat);

        boolean seatUsedByAnotherPassenger =
                passengerRepository
                        .existsByBookingIdAndSeatIdAndIdNot(
                                booking.getId(),
                                seat.getId(),
                                id
                        );

        if (seatUsedByAnotherPassenger) {
            throw new RuntimeException(
                    "This seat is already assigned to another passenger"
            );
        }

        existingPassenger.setName(request.getName());
        existingPassenger.setAge(request.getAge());
        existingPassenger.setGender(request.getGender());
        existingPassenger.setNic(request.getNic());
        existingPassenger.setBooking(booking);
        existingPassenger.setSeat(seat);

        return passengerRepository.save(existingPassenger);
    }

    // DELETE passenger
    public void deletePassenger(Long id) {

        Passenger passenger =
                passengerRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Passenger not found"));

        passengerRepository.delete(passenger);
    }

    // Check whether the selected seat belongs to this booking
    private void validateSeatSelectedForBooking(
            Booking booking,
            Seat seat) {

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
    }
}