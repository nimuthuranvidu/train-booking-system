package com.trainbooking.repository;

import com.trainbooking.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationRepository extends  JpaRepository<Station, Long>{
    Optional<Station> findByCode(String code);

    Optional<Station> findByName(String name);

}
