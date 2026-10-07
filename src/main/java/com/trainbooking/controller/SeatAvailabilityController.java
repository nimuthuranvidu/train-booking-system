package com.trainbooking.controller;

import com.trainbooking.dto.SeatAvailabilityResponse;
import com.trainbooking.service.SeatAvailabilityService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/seat-availability")
public class SeatAvailabilityController {

    private final SeatAvailabilityService seatAvailabilityService;

    public SeatAvailabilityController(
            SeatAvailabilityService seatAvailabilityService) {

        this.seatAvailabilityService = seatAvailabilityService;
    }

    @GetMapping
    public List<SeatAvailabilityResponse> getSeats(
            @RequestParam Long scheduleId,
            @RequestParam LocalDate travelDate) {

        return seatAvailabilityService.getAvailableSeats(
                scheduleId,
                travelDate
        );
    }
}