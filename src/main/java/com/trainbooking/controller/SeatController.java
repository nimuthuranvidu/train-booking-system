package com.trainbooking.controller;

import com.trainbooking.entity.Seat;
import com.trainbooking.service.SeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping
    public Seat addSeat(@RequestBody Seat seat) {
        return seatService.addSeat(seat);
    }

    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    @GetMapping("/coach/{coachId}")
    public List<Seat> getSeatsByCoach(@PathVariable Long coachId) {
        return seatService.getSeatsByCoach(coachId);
    }
}