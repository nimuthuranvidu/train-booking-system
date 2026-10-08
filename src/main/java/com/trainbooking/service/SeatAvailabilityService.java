package com.trainbooking.service;

import com.trainbooking.dto.SeatAvailabilityResponse;
import com.trainbooking.entity.Schedule;
import com.trainbooking.entity.Seat;
import com.trainbooking.repository.BookingSeatRepository;
import com.trainbooking.repository.ScheduleRepository;
import com.trainbooking.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeatAvailabilityService {

    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public SeatAvailabilityService(
            ScheduleRepository scheduleRepository,
            SeatRepository seatRepository,
            BookingSeatRepository bookingSeatRepository) {

        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    public List<SeatAvailabilityResponse> getAvailableSeats(
            Long scheduleId,
            LocalDate travelDate) {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() ->
                        new RuntimeException("Schedule not found"));

        List<Seat> seats =
                seatRepository.findByCoachTrainId(
                        schedule.getTrain().getId()
                );

        List<SeatAvailabilityResponse> results =
                new ArrayList<>();

        for (Seat seat : seats) {

            boolean booked =
                    bookingSeatRepository
                            .existsBySeatIdAndScheduleIdAndTravelDate(
                                    seat.getId(),
                                    schedule.getId(),
                                    travelDate
                            );

            results.add(
                    new SeatAvailabilityResponse(
                            seat.getId(),
                            seat.getCoach().getCoachNumber(),
                            seat.getSeatNumber(),
                            seat.getSeatType(),
                            !booked
                    )
            );
        }

        return results;
    }
}