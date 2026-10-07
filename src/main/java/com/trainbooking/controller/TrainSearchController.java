package com.trainbooking.controller;

import com.trainbooking.dto.TrainSearchRequest;
import com.trainbooking.dto.TrainSearchResponse;
import com.trainbooking.service.TrainSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/train-search")
public class TrainSearchController {

    private final TrainSearchService trainSearchService;

    public TrainSearchController(TrainSearchService trainSearchService) {
        this.trainSearchService = trainSearchService;
    }

    @PostMapping
    public List<TrainSearchResponse> searchTrains(
            @RequestBody TrainSearchRequest request) {

        return trainSearchService.searchTrains(request);
    }
}