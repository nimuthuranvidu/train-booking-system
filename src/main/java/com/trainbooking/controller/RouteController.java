package com.trainbooking.controller;

import com.trainbooking.entity.Route;
import com.trainbooking.service.RouteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // =========================
    // ADD ROUTE
    // =========================

    @PostMapping
    public Route addRoute(@RequestBody Route route) {
        return routeService.addRoute(route);
    }

    // =========================
    // GET ALL ROUTES
    // =========================

    @GetMapping
    public List<Route> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    // =========================
    // UPDATE ROUTE
    // =========================

    @PutMapping("/{id}")
    public Route updateRoute(
            @PathVariable Long id,
            @RequestBody Route route) {

        return routeService.updateRoute(id, route);
    }

    // =========================
    // DELETE ROUTE
    // =========================

    @DeleteMapping("/{id}")
    public String deleteRoute(@PathVariable Long id) {

        routeService.deleteRoute(id);

        return "Route deleted successfully";
    }
}