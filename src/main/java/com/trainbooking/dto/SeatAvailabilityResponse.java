package com.trainbooking.dto;

public class SeatAvailabilityResponse {

    private Long seatId;
    private String coachNumber;
    private String seatNumber;
    private String seatType;
    private boolean available;

    public SeatAvailabilityResponse() {
    }

    public SeatAvailabilityResponse(
            Long seatId,
            String coachNumber,
            String seatNumber,
            String seatType,
            boolean available) {

        this.seatId = seatId;
        this.coachNumber = coachNumber;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.available = available;
    }

    public Long getSeatId() {
        return seatId;
    }

    public String getCoachNumber() {
        return coachNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

    public boolean isAvailable() {
        return available;
    }
}