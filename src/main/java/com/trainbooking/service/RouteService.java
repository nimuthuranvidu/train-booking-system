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

    // =========================
    // ADD ROUTE
    // =========================

    public Route addRoute(Route route) {

        validateRoute(route);

        return routeRepository.save(route);
    }

    // =========================
    // GET ALL ROUTES
    // =========================

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    // =========================
    // UPDATE ROUTE
    // =========================

    public Route updateRoute(Long id, Route updatedRoute) {

        validateRoute(updatedRoute);

        Route existingRoute = routeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Route not found"));

        existingRoute.setSourceStation(
                updatedRoute.getSourceStation()
        );

        existingRoute.setDestinationStation(
                updatedRoute.getDestinationStation()
        );

        return routeRepository.save(existingRoute);
    }

    // =========================
    // DELETE ROUTE
    // =========================

    public void deleteRoute(Long id) {

        Route route = routeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Route not found"));

        routeRepository.delete(route);
    }

    // =========================
    // VALIDATE ROUTE
    // =========================

    private void validateRoute(Route route) {

        if (route.getSourceStation() == null ||
                route.getDestinationStation() == null) {

            throw new RuntimeException(
                    "Source and destination stations are required"
            );
        }

        if (route.getSourceStation().getId()
                .equals(route.getDestinationStation().getId())) {

            throw new RuntimeException(
                    "Source and destination stations cannot be the same"
            );
        }
    }
}