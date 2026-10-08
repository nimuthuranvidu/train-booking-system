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

    // ADD coach
    @PostMapping
    public Coach addCoach(@RequestBody Coach coach) {
        return coachService.addCoach(coach);
    }

    // VIEW all coaches
    @GetMapping
    public List<Coach> getAllCoaches() {
        return coachService.getAllCoaches();
    }

    // VIEW coaches by train
    @GetMapping("/train/{trainId}")
    public List<Coach> getCoachesByTrain(@PathVariable Long trainId) {
        return coachService.getCoachesByTrain(trainId);
    }

    // GENERATE coaches and seats
    @PostMapping("/generate")
    public List<Coach> generateCoaches(
            @RequestBody CoachGenerationRequest request) {

        return coachService.generateCoaches(request);
    }

    // UPDATE coach
    @PutMapping("/{id}")
    public Coach updateCoach(
            @PathVariable Long id,
            @RequestBody Coach coach) {

        return coachService.updateCoach(id, coach);
    }

    // DELETE coach
    @DeleteMapping("/{id}")
    public String deleteCoach(@PathVariable Long id) {

        coachService.deleteCoach(id);

        return "Coach deleted successfully";
    }
}