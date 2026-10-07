package com.trainbooking.repository;

import com.trainbooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Get all seats belonging to a specific coach
    List<Seat> findByCoachId(Long coachId);

    // Find a specific seat inside a specific coach
    Optional<Seat> findBySeatNumberAndCoachId(
            String seatNumber,
            Long coachId
    );

    // Get all seats belonging to all coaches of a specific train
    List<Seat> findByCoachTrainId(Long trainId);
}