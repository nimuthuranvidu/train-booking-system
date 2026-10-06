package com.trainbooking.service;

import com.trainbooking.entity.Seat;
import com.trainbooking.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat addSeat(Seat seat) {

        if (seatRepository.findBySeatNumberAndCoachId(
                seat.getSeatNumber(),
                seat.getCoach().getId()
        ).isPresent()) {

            throw new RuntimeException(
                    "Seat number already exists in this coach"
            );
        }

        return seatRepository.save(seat);
    }

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public List<Seat> getSeatsByCoach(Long coachId) {
        return seatRepository.findByCoachId(coachId);
    }
}