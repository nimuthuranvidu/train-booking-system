package com.trainbooking.dto;

public class CoachGenerationRequest {

    private Long trainId;
    private int numberOfCoaches;
    private int defaultSeatsPerCoach;

    public CoachGenerationRequest() {
    }

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public int getNumberOfCoaches() {
        return numberOfCoaches;
    }

    public void setNumberOfCoaches(int numberOfCoaches) {
        this.numberOfCoaches = numberOfCoaches;
    }

    public int getDefaultSeatsPerCoach() {
        return defaultSeatsPerCoach;
    }

    public void setDefaultSeatsPerCoach(int defaultSeatsPerCoach) {
        this.defaultSeatsPerCoach = defaultSeatsPerCoach;
    }
}