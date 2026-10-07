package com.trainbooking.dto;

import java.time.LocalTime;

public class TrainSearchResponse {

    private Long scheduleId;
    private String trainNumber;
    private String trainName;

    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private int totalSeats;
    private int availableSeats;

    private boolean enoughSeats;

    public TrainSearchResponse() {
    }

    public TrainSearchResponse(
            Long scheduleId,
            String trainNumber,
            String trainName,
            LocalTime departureTime,
            LocalTime arrivalTime,
            int totalSeats,
            int availableSeats,
            boolean enoughSeats) {

        this.scheduleId = scheduleId;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.enoughSeats = enoughSeats;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public boolean isEnoughSeats() {
        return enoughSeats;
    }
}