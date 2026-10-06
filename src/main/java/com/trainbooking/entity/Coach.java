package com.trainbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coaches")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String coachNumber;

    @Column(nullable = false)
    private String coachType;

    @Column(nullable = false)
    private Integer seatCapacity;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    public Coach() {
    }

    public Coach(String coachNumber, String coachType, Integer seatCapacity, Train train) {
        this.coachNumber = coachNumber;
        this.coachType = coachType;
        this.seatCapacity = seatCapacity;
        this.train = train;
    }

    public Long getId() {
        return id;
    }

    public String getCoachNumber() {
        return coachNumber;
    }

    public void setCoachNumber(String coachNumber) {
        this.coachNumber = coachNumber;
    }

    public String getCoachType() {
        return coachType;
    }

    public void setCoachType(String coachType) {
        this.coachType = coachType;
    }

    public Integer getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(Integer seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }
}