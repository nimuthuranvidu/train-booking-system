package com.trainbooking.service;

import com.trainbooking.entity.Schedule;
import com.trainbooking.entity.ScheduleDay;
import com.trainbooking.repository.BookingRepository;
import com.trainbooking.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final BookingRepository bookingRepository;

    public ScheduleService(
            ScheduleRepository scheduleRepository,
            BookingRepository bookingRepository) {

        this.scheduleRepository = scheduleRepository;
        this.bookingRepository = bookingRepository;
    }

    // ADD schedule
    public Schedule addSchedule(Schedule schedule) {

        validateSchedule(schedule);

        for (ScheduleDay day : schedule.getOperatingDays()) {
            day.setSchedule(schedule);
        }

        return scheduleRepository.save(schedule);
    }

    // VIEW all schedules
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // UPDATE schedule
    public Schedule updateSchedule(
            Long id,
            Schedule updatedSchedule) {

        validateSchedule(updatedSchedule);

        Schedule existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Schedule not found"));

        existingSchedule.setTrain(updatedSchedule.getTrain());
        existingSchedule.setRoute(updatedSchedule.getRoute());
        existingSchedule.setDepartureTime(
                updatedSchedule.getDepartureTime()
        );
        existingSchedule.setArrivalTime(
                updatedSchedule.getArrivalTime()
        );

        existingSchedule.getOperatingDays().clear();

        for (ScheduleDay day : updatedSchedule.getOperatingDays()) {
            day.setSchedule(existingSchedule);
            existingSchedule.getOperatingDays().add(day);
        }

        return scheduleRepository.save(existingSchedule);
    }

    // DELETE schedule
    public void deleteSchedule(Long id) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Schedule not found"));

        if (bookingRepository.existsByScheduleId(id)) {
            throw new RuntimeException(
                    "Cannot delete schedule because bookings exist for this schedule"
            );
        }

        scheduleRepository.delete(schedule);
    }

    // VALIDATION
    private void validateSchedule(Schedule schedule) {

        if (schedule.getDepartureTime() == null ||
                schedule.getArrivalTime() == null) {

            throw new RuntimeException(
                    "Departure time and arrival time are required"
            );
        }

        if (schedule.getDepartureTime()
                .isAfter(schedule.getArrivalTime())) {

            throw new RuntimeException(
                    "Departure time cannot be after arrival time"
            );
        }

        if (schedule.getTrain() == null) {
            throw new RuntimeException(
                    "Train is required"
            );
        }

        if (schedule.getRoute() == null) {
            throw new RuntimeException(
                    "Route is required"
            );
        }

        if (schedule.getOperatingDays() == null ||
                schedule.getOperatingDays().isEmpty()) {

            throw new RuntimeException(
                    "At least one operating day is required"
            );
        }
    }
}