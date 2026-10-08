package com.trainbooking.controller;

import com.trainbooking.entity.Train;
import com.trainbooking.service.TrainService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @PostMapping
    public Train addTrain(@RequestBody Train train) {
        return trainService.addTrain(train);
    }

    @GetMapping
    public List<Train> getAllTrains() {
        return trainService.getAllTrains();
    }

    @PutMapping("/{id}")
    public Train updateTrain(
            @PathVariable Long id,
            @RequestBody Train train) {

        return trainService.updateTrain(id, train);
    }

    @DeleteMapping("/{id}")
    public String deleteTrain(@PathVariable Long id) {

        trainService.deleteTrain(id);

        return "Train deleted successfully";
    }
}