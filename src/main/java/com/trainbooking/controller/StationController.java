package com.trainbooking.controller;

import com.trainbooking.entity.Station;
import com.trainbooking.service.StationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService){
        this.stationService = stationService;
    }

    @PostMapping
    public Station addStation(@RequestBody Station station){
        return stationService.addStation(station);
    }

    @GetMapping
    public List<Station> getAllStations(){
        return stationService.getAllStations();
    }

}
