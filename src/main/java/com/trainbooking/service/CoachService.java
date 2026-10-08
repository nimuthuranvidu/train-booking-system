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

    // ADD coach
    public Coach addCoach(Coach coach) {

        if (coach.getTrain() == null ||
                coach.getTrain().getId() == null) {

            throw new RuntimeException("Train is required");
        }

        if (coach.getCoachNumber() == null ||
                coach.getCoachNumber().isBlank()) {

            throw new RuntimeException("Coach number is required");
        }

        if (coach.getSeatCapacity() <= 0) {
            throw new RuntimeException(
                    "Seat capacity must be greater than 0"
            );
        }

        trainRepository.findById(coach.getTrain().getId())
                .orElseThrow(() ->
                        new RuntimeException("Train not found"));

        if (coachRepository.findByCoachNumberAndTrainId(
                coach.getCoachNumber(),
                coach.getTrain().getId()
        ).isPresent()) {

            throw new RuntimeException(
                    "Coach number already exists for this train"
            );
        }

        return coachRepository.save(coach);
    }

    // VIEW all coaches
    public List<Coach> getAllCoaches() {
        return coachRepository.findAll();
    }

    // VIEW coaches by train
    public List<Coach> getCoachesByTrain(Long trainId) {

        trainRepository.findById(trainId)
                .orElseThrow(() ->
                        new RuntimeException("Train not found"));

        return coachRepository.findByTrainId(trainId);
    }

    // UPDATE coach
    public Coach updateCoach(Long id, Coach updatedCoach) {

        Coach existingCoach = coachRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Coach not found"));

        if (updatedCoach.getTrain() == null ||
                updatedCoach.getTrain().getId() == null) {

            throw new RuntimeException("Train is required");
        }

        if (updatedCoach.getCoachNumber() == null ||
                updatedCoach.getCoachNumber().isBlank()) {

            throw new RuntimeException("Coach number is required");
        }

        if (updatedCoach.getSeatCapacity() <= 0) {
            throw new RuntimeException(
                    "Seat capacity must be greater than 0"
            );
        }

        Train train = trainRepository.findById(
                updatedCoach.getTrain().getId()
        ).orElseThrow(() ->
                new RuntimeException("Train not found"));

        if (!existingCoach.getCoachNumber().equals(
                updatedCoach.getCoachNumber())
                || !existingCoach.getTrain().getId().equals(
                train.getId())) {

            if (coachRepository.findByCoachNumberAndTrainId(
                    updatedCoach.getCoachNumber(),
                    train.getId()
            ).isPresent()) {

                throw new RuntimeException(
                        "Coach number already exists for this train"
                );
            }
        }

        existingCoach.setCoachNumber(
                updatedCoach.getCoachNumber()
        );

        existingCoach.setCoachType(
                updatedCoach.getCoachType()
        );

        existingCoach.setSeatCapacity(
                updatedCoach.getSeatCapacity()
        );

        existingCoach.setTrain(train);

        return coachRepository.save(existingCoach);
    }

    // DELETE coach
    public void deleteCoach(Long id) {

        Coach coach = coachRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Coach not found"));

        List<Seat> seats = seatRepository.findByCoachId(id);

        if (!seats.isEmpty()) {
            seatRepository.deleteAll(seats);
        }

        coachRepository.delete(coach);
    }

    // GENERATE coaches and seats automatically
    public List<Coach> generateCoaches(
            CoachGenerationRequest request) {

        if (request.getNumberOfCoaches() <= 0) {
            throw new RuntimeException(
                    "Number of coaches must be greater than 0"
            );
        }

        if (request.getDefaultSeatsPerCoach() <= 0) {
            throw new RuntimeException(
                    "Seats per coach must be greater than 0"
            );
        }

        Train train = trainRepository.findById(
                request.getTrainId()
        ).orElseThrow(() ->
                new RuntimeException("Train not found"));

        List<Coach> generatedCoaches = new ArrayList<>();

        for (int i = 1;
             i <= request.getNumberOfCoaches();
             i++) {

            String coachNumber = "S" + i;

            if (coachRepository.findByCoachNumberAndTrainId(
                    coachNumber,
                    train.getId()
            ).isPresent()) {

                throw new RuntimeException(
                        coachNumber +
                                " already exists for this train"
                );
            }

            Coach coach = new Coach(
                    coachNumber,
                    "SECOND_CLASS",
                    request.getDefaultSeatsPerCoach(),
                    train
            );

            Coach savedCoach =
                    coachRepository.save(coach);

            for (int j = 1;
                 j <= request.getDefaultSeatsPerCoach();
                 j++) {

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