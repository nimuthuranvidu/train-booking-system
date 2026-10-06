package com.trainbooking.controller;

import com.trainbooking.dto.CoachGenerationRequest;
import com.trainbooking.entity.Coach;
import com.trainbooking.service.CoachService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coaches")
public class CoachController {

    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @PostMapping
    public Coach addCoach(@RequestBody Coach coach) {
        return coachService.addCoach(coach);
    }

    @GetMapping
    public List<Coach> getAllCoaches() {
        return coachService.getAllCoaches();
    }

    @GetMapping("/train/{trainId}")
    public List<Coach> getCoachesByTrain(@PathVariable Long trainId) {
        return coachService.getCoachesByTrain(trainId);
    }

    @PostMapping("/generate")
    public List<Coach> generateCoaches(
            @RequestBody CoachGenerationRequest request) {

        return coachService.generateCoaches(request);
    }
}