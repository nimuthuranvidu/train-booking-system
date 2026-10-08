package com.trainbooking.service;

import com.trainbooking.entity.Seat;
import com.trainbooking.repository.BookingSeatRepository;
import com.trainbooking.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public SeatService(
            SeatRepository seatRepository,
            BookingSeatRepository bookingSeatRepository) {

        this.seatRepository = seatRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    // ADD seat
    public Seat addSeat(Seat seat) {

        if (seat.getCoach() == null ||
                seat.getCoach().getId() == null) {

            throw new RuntimeException("Coach is required");
        }

        if (seat.getSeatNumber() == null ||
                seat.getSeatNumber().isBlank()) {

            throw new RuntimeException("Seat number is required");
        }

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

    // VIEW all seats
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    // VIEW seats by coach
    public List<Seat> getSeatsByCoach(Long coachId) {
        return seatRepository.findByCoachId(coachId);
    }

    // UPDATE seat
    public Seat updateSeat(Long id, Seat updatedSeat) {

        Seat existingSeat = seatRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Seat not found"));

        if (updatedSeat.getSeatNumber() == null ||
                updatedSeat.getSeatNumber().isBlank()) {

            throw new RuntimeException("Seat number is required");
        }

        if (updatedSeat.getCoach() == null ||
                updatedSeat.getCoach().getId() == null) {

            throw new RuntimeException("Coach is required");
        }

        if (!existingSeat.getSeatNumber().equals(
                updatedSeat.getSeatNumber())
                || !existingSeat.getCoach().getId().equals(
                updatedSeat.getCoach().getId())) {

            if (seatRepository.findBySeatNumberAndCoachId(
                    updatedSeat.getSeatNumber(),
                    updatedSeat.getCoach().getId()
            ).isPresent()) {

                throw new RuntimeException(
                        "Seat number already exists in this coach"
                );
            }
        }

        existingSeat.setSeatNumber(
                updatedSeat.getSeatNumber()
        );

        existingSeat.setSeatType(
                updatedSeat.getSeatType()
        );

        existingSeat.setCoach(
                updatedSeat.getCoach()
        );

        return seatRepository.save(existingSeat);
    }

    // DELETE seat
    public void deleteSeat(Long id) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Seat not found"));

        if (bookingSeatRepository.existsBySeatId(id)) {
            throw new RuntimeException(
                    "Cannot delete seat because it has booking records"
            );
        }

        seatRepository.delete(seat);
    }
}