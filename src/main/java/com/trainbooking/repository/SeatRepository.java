package com.trainbooking.repository;

import com.trainbooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByCoachId(Long coachId);

    Optional<Seat> findBySeatNumberAndCoachId(
            String seatNumber,
            Long coachId
    );
}