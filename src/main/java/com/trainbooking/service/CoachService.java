package com.trainbooking.service;

import com.trainbooking.dto.CoachGenerationRequest;
import com.trainbooking.entity.Coach;
import com.trainbooking.entity.Seat;
import com.trainbooking.entity.Train;
import com.trainbooking.repository.CoachRepository;
import com.trainbooking.repository.SeatRepository;
import com.trainbooking.repository.TrainRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoachService {

    private final CoachRepository coachRepository;
    private final SeatRepository seatRepository;
    private final TrainRepository trainRepository;

    public CoachService(
            CoachRepository coachRepository,
            SeatRepository seatRepository,
            TrainRepository trainRepository) {

        this.coachRepository = coachRepository;
        this.seatRepository = seatRepository;
        this.trainRepository = trainRepository;
    }

    public Coach addCoach(Coach coach) {

        if (coachRepository.findByCoachNumberAndTrainId(
                coach.getCoachNumber(),
                coach.getTrain().getId()
        ).isPresent()) {

            throw new RuntimeException(
                    "Coach number already exists for this train"
            );
        }

        if (coach.getSeatCapacity() <= 0) {
            throw new RuntimeException("Seat capacity must be greater than 0");
        }

        return coachRepository.save(coach);
    }

    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }

    public List<Coach> getCoachesByTrain(Long trainId) {
        return coachRepository.findByTrainId(trainId);
    }

    // Generate coaches and seats automatically
    public List<Coach> generateCoaches(CoachGenerationRequest request) {

        if (request.getNumberOfCoaches() <= 0) {
            throw new RuntimeException("Number of coaches must be greater than 0");
        }

        if (request.getDefaultSeatsPerCoach() <= 0) {
            throw new RuntimeException("Seats per coach must be greater than 0");
        }

        Train train = trainRepository.findById(request.getTrainId())
                .orElseThrow(() -> new RuntimeException("Train not found"));

        List<Coach> generatedCoaches = new ArrayList<>();

        for (int i = 1; i <= request.getNumberOfCoaches(); i++) {

            String coachNumber = "S" + i;

            // Check if this coach already exists for this train
            if (coachRepository.findByCoachNumberAndTrainId(
                    coachNumber,
                    train.getId()
            ).isPresent()) {

                throw new RuntimeException(
                        coachNumber + " already exists for this train"
                );
            }

            Coach coach = new Coach(
                    coachNumber,
                    "SECOND_CLASS",
                    request.getDefaultSeatsPerCoach(),
                    train
            );

            Coach savedCoach = coachRepository.save(coach);

            for (int j = 1; j <= request.getDefaultSeatsPerCoach(); j++) {

                Seat seat = new Seat(
                        String.valueOf(j),
                        "WINDOW",
                        savedCoach
                );

                seatRepository.save(seat);
            }

            generatedCoaches.add(savedCoach);
        }

        return generatedCoaches;
    }
}