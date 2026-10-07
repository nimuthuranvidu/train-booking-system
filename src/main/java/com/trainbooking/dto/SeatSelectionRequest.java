package com.trainbooking.dto;

import java.util.List;

public class SeatSelectionRequest {

    private Long bookingId;
    private List<Long> seatIds;

    public SeatSelectionRequest() {
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }
}