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

    // ADD schedule
    @PostMapping
    public Schedule addSchedule(@RequestBody Schedule schedule) {
        return scheduleService.addSchedule(schedule);
    }

    // VIEW all schedules
    @GetMapping
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    // UPDATE schedule
    @PutMapping("/{id}")
    public Schedule updateSchedule(
            @PathVariable Long id,
            @RequestBody Schedule schedule) {

        return scheduleService.updateSchedule(id, schedule);
    }

    // DELETE schedule
    @DeleteMapping("/{id}")
    public String deleteSchedule(@PathVariable Long id) {

        scheduleService.deleteSchedule(id);

        return "Schedule deleted successfully";
    }
}