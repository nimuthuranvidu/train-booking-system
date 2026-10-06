package com.trainbooking.service;

import com.trainbooking.entity.Route;
import com.trainbooking.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Route addRoute(Route route) {

        if (route.getSourceStation().getId()
                .equals(route.getDestinationStation().getId())) {

            throw new RuntimeException(
                    "Source and destination stations cannot be the same"
            );
        }

        return routeRepository.save(route);
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }
}