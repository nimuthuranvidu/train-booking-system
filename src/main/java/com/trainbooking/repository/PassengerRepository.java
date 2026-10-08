package com.trainbooking.repository;

import com.trainbooking.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    List<Passenger> findByBookingId(Long bookingId);

    boolean existsByBookingIdAndSeatId(
            Long bookingId,
            Long seatId
    );

    boolean existsByBookingIdAndSeatIdAndIdNot(
            Long bookingId,
            Long seatId,
            Long passengerId
    );
}