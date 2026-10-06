package com.trainbooking.repository;

import com.trainbooking.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, Long> {

    Optional<Coach> findByCoachNumberAndTrainId(
            String coachNumber,
            Long trainId
    );

    List<Coach> findByTrainId(Long trainId);
}