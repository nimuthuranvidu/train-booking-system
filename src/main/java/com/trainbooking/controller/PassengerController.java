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

    // ADD passenger
    @PostMapping
    public Passenger addPassenger(
            @RequestBody PassengerRequest request) {

        return passengerService.addPassenger(request);
    }

    // VIEW all passengers
    @GetMapping
    public List<Passenger> getAllPassengers() {
        return passengerService.getAllPassengers();
    }

    // VIEW passengers by booking
    @GetMapping("/booking/{bookingId}")
    public List<Passenger> getPassengersByBooking(
            @PathVariable Long bookingId) {

        return passengerService.getPassengersByBooking(bookingId);
    }

    // UPDATE passenger
    @PutMapping("/{id}")
    public Passenger updatePassenger(
            @PathVariable Long id,
            @RequestBody PassengerRequest request) {

        return passengerService.updatePassenger(id, request);
    }

    // DELETE passenger
    @DeleteMapping("/{id}")
    public String deletePassenger(@PathVariable Long id) {

        passengerService.deletePassenger(id);

        return "Passenger deleted successfully";
    }
}