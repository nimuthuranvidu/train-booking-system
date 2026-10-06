package com.trainbooking.service;

import com.trainbooking.entity.Schedule;
import com.trainbooking.entity.ScheduleDay;
import com.trainbooking.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule addSchedule(Schedule schedule) {

        if (schedule.getDepartureTime().isAfter(schedule.getArrivalTime())) {
            throw new RuntimeException(
                    "Departure time cannot be after arrival time"
            );
        }

        for (ScheduleDay day : schedule.getOperatingDays()) {
            day.setSchedule(schedule);
        }

        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
}