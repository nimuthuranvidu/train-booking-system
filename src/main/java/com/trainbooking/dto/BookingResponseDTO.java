package com.trainbooking.dto;

import java.time.LocalDate;

public class BookingResponseDTO {

    private Long id;
    private UserResponseDTO user;
    private Long scheduleId;
    private LocalDate travelDate;
    private String status;
    private double totalAmount;

    public BookingResponseDTO() {
    }

    public BookingResponseDTO(
            Long id,
            UserResponseDTO user,
            Long scheduleId,
            LocalDate travelDate,
            String status,
            double totalAmount) {

        this.id = id;
        this.user = user;
        this.scheduleId = scheduleId;
        this.travelDate = travelDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}