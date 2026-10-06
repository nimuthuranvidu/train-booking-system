package com.trainbooking.entity;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @OneToMany(
            mappedBy = "schedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ScheduleDay> operatingDays = new ArrayList<>();

    public Schedule() {
    }

    public Long getId() {
        return id;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<ScheduleDay> getOperatingDays() {
        return operatingDays;
    }

    public void setOperatingDays(List<ScheduleDay> operatingDays) {
        this.operatingDays = operatingDays;
    }
}