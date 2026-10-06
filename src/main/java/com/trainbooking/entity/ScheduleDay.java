package com.trainbooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.DayOfWeek;

@Entity
@Table(name = "schedule_days")
public class ScheduleDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek day;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    @JsonIgnore
    private Schedule schedule;

    public ScheduleDay() {
    }

    public ScheduleDay(DayOfWeek day, Schedule schedule) {
        this.day = day;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}