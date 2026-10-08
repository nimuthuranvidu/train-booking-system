package com.trainbooking.dto;

public class BookingSeatResponseDTO {

    private Long id;
    private Long bookingId;
    private Long seatId;
    private String coachNumber;
    private String seatNumber;
    private String seatType;

    public BookingSeatResponseDTO() {
    }

    public BookingSeatResponseDTO(
            Long id,
            Long bookingId,
            Long seatId,
            String coachNumber,
            String seatNumber,
            String seatType) {

        this.id = id;
        this.bookingId = bookingId;
        this.seatId = seatId;
        this.coachNumber = coachNumber;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
    }

    public Long getId() {
        return id;
    }

    public Long getBookingId() {
        return bookingId;
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
}