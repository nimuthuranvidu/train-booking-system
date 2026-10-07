package com.trainbooking.controller;

import com.trainbooking.dto.PassengerRequest;
import com.trainbooking.entity.Passenger;
import com.trainbooking.service.PassengerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping
    public Passenger addPassenger(
            @RequestBody PassengerRequest request) {

        return passengerService.addPassenger(request);
    }

    @GetMapping
    public List<Passenger> getAllPassengers() {
        return passengerService.getAllPassengers();
    }

    @GetMapping("/booking/{bookingId}")
    public List<Passenger> getPassengersByBooking(
            @PathVariable Long bookingId) {

        return passengerService.getPassengersByBooking(bookingId);
    }
}