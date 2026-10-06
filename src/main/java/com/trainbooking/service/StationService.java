package com.trainbooking.service;

import com.trainbooking.entity.Station;
import com.trainbooking.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository){
        this.stationRepository = stationRepository;
    }

    public Station addStation(Station station){

        if(stationRepository.findByCode(station.getCode()).isPresent()){
            throw new RuntimeException("Station code already exists");
        }
        if(stationRepository.findByName(station.getName()).isPresent()){
            throw new RuntimeException("Station already exists");
        }

        return stationRepository.save(station);

    }

    public List<Station> getAllStations(){
        return stationRepository.findAll();
    }

}

