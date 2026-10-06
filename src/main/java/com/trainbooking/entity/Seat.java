package com.trainbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private String seatType;

    @Column(nullable = false)
    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    public Seat() {
    }

    public Seat(String seatNumber, String seatType, Coach coach) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.coach = coach;
    }

    public Long getId() {
        return id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }
}