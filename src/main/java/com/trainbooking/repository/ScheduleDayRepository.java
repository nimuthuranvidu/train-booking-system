package com.trainbooking.repository;

import com.trainbooking.entity.ScheduleDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleDayRepository extends JpaRepository<ScheduleDay, Long> {
}