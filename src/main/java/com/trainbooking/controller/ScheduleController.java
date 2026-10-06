package com.trainbooking.controller;

import com.trainbooking.entity.Schedule;
import com.trainbooking.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public Schedule addSchedule(@RequestBody Schedule schedule) {
        return scheduleService.addSchedule(schedule);
    }

    @GetMapping
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }
}