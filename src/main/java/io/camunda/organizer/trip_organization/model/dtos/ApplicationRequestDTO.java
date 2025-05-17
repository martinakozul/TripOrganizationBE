package io.camunda.organizer.trip_organization.model.dtos;

import java.util.List;

public class ApplicationRequestDTO {

    private String email;
    private String phoneNumber;
    private int numberOfTravelers;
    private List<String> peopleInformation;
    private Long tripId;

    public ApplicationRequestDTO(String email, String phoneNumber, int numberOfTravelers, List<String> peopleInformation, Long tripId) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.numberOfTravelers = numberOfTravelers;
        this.peopleInformation = peopleInformation;
        this.tripId = tripId;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getNumberOfTravelers() {
        return numberOfTravelers;
    }

    public List<String> getPeopleInformation() {
        return peopleInformation;
    }

    public Long getTripId() {
        return tripId;
    }
}
