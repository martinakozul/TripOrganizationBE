package io.camunda.organizer.trip_organization.model.dtos;

public class BasicTrip {

    private long tripId;
    private String tripName;
    private String taskName;

    public BasicTrip(long tripId, String taskName) {
        this.tripId = tripId;
        this.taskName = taskName;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public long getTripId() {
        return tripId;
    }

    public String getTaskName() {
        return taskName;
    }
}
