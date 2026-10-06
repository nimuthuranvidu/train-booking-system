package com.trainbooking.service;

import com.trainbooking.entity.Train;
import com.trainbooking.repository.TrainRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {

    private final TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public Train addTrain(Train train) {

        if (trainRepository.findByTrainNumber(train.getTrainNumber()).isPresent()) {
            throw new RuntimeException("Train number already exists");
        }

        return trainRepository.save(train);
    }

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }
}