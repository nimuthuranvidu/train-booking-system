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

    // ADD seat
    @PostMapping
    public Seat addSeat(@RequestBody Seat seat) {
        return seatService.addSeat(seat);
    }

    // VIEW all seats
    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    // VIEW seats by coach
    @GetMapping("/coach/{coachId}")
    public List<Seat> getSeatsByCoach(@PathVariable Long coachId) {
        return seatService.getSeatsByCoach(coachId);
    }

    // UPDATE seat
    @PutMapping("/{id}")
    public Seat updateSeat(
            @PathVariable Long id,
            @RequestBody Seat seat) {

        return seatService.updateSeat(id, seat);
    }

    // DELETE seat
    @DeleteMapping("/{id}")
    public String deleteSeat(@PathVariable Long id) {

        seatService.deleteSeat(id);

        return "Seat deleted successfully";
    }
}