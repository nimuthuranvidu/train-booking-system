package com.trainbooking.service;

import com.trainbooking.dto.TrainSearchRequest;
import com.trainbooking.dto.TrainSearchResponse;
import com.trainbooking.entity.Schedule;
import com.trainbooking.entity.Seat;
import com.trainbooking.repository.BookingSeatRepository;
import com.trainbooking.repository.ScheduleRepository;
import com.trainbooking.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainSearchService {

    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public TrainSearchService(
            ScheduleRepository scheduleRepository,
            SeatRepository seatRepository,
            BookingSeatRepository bookingSeatRepository) {

        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    public List<TrainSearchResponse> searchTrains(
            TrainSearchRequest request) {

        // Validate passenger count
        if (request.getNumberOfPassengers() <= 0) {
            throw new RuntimeException(
                    "Number of passengers must be greater than 0"
            );
        }

        // Validate travel date
        if (request.getTravelDate() == null) {
            throw new RuntimeException(
                    "Travel date is required"
            );
        }

        LocalDate travelDate = request.getTravelDate();

        // Get the day of the week
        DayOfWeek travelDay = travelDate.getDayOfWeek();

        List<TrainSearchResponse> results = new ArrayList<>();

        // Get all schedules
        List<Schedule> schedules = scheduleRepository.findAll();

        for (Schedule schedule : schedules) {

            // Check route
            if (!schedule.getRoute()
                    .getSourceStation()
                    .getId()
                    .equals(request.getSourceStationId())) {

                continue;
            }

            if (!schedule.getRoute()
                    .getDestinationStation()
                    .getId()
                    .equals(request.getDestinationStationId())) {

                continue;
            }

            // Check operating day
            boolean operatesOnDay = schedule.getOperatingDays()
                    .stream()
                    .anyMatch(day ->
                            day.getDay().equals(travelDay)
                    );

            if (!operatesOnDay) {
                continue;
            }

            // Get all seats for this train
            List<Seat> seats =
                    seatRepository.findByCoachTrainId(
                            schedule.getTrain().getId()
                    );

            int totalSeats = seats.size();
            int bookedSeats = 0;

            // Check each seat for this travel date
            for (Seat seat : seats) {

                boolean booked =
                        bookingSeatRepository
                                .existsBySeatIdAndTravelDate(
                                        seat.getId(),
                                        travelDate
                                );

                if (booked) {
                    bookedSeats++;
                }
            }

            int availableSeats = totalSeats - bookedSeats;

            boolean enoughSeats =
                    availableSeats >= request.getNumberOfPassengers();

            TrainSearchResponse response =
                    new TrainSearchResponse(
                            schedule.getId(),
                            schedule.getTrain().getTrainNumber(),
                            schedule.getTrain().getName(),
                            schedule.getDepartureTime(),
                            schedule.getArrivalTime(),
                            totalSeats,
                            availableSeats,
                            enoughSeats
                    );

            results.add(response);
        }

        return results;
    }
}