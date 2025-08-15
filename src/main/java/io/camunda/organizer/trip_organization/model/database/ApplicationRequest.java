package io.camunda.organizer.trip_organization.model.database;

import io.camunda.organizer.trip_organization.model.dtos.ApplicationRequestDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "applications")
public class ApplicationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String phoneNumber;
    private int numberOfTravelers;
    private List<String> peopleInformation;
    private Boolean isConfirmed;
    private Boolean isPaid;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private TripInformation tripInformation;

    public ApplicationRequest() {
    }

    public ApplicationRequest(Long id, String email, String phoneNumber, int numberOfTravelers, List<String> peopleInformation, Long tripId) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.numberOfTravelers = numberOfTravelers;
        this.peopleInformation = peopleInformation;
    }

    public ApplicationRequest(ApplicationRequestDTO request) {
        this.email = request.getEmail();
        this.phoneNumber = request.getPhoneNumber();
        this.numberOfTravelers = request.getNumberOfTravelers();
        this.peopleInformation = request.getPeopleInformation();
        this.isConfirmed = false;
        this.isPaid = false;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getNumberOfTravelers() {
        return numberOfTravelers;
    }

    public void setNumberOfTravelers(int numberOfTravelers) {
        this.numberOfTravelers = numberOfTravelers;
    }

    public List<String> getPeopleInformation() {
        return peopleInformation;
    }

    public void setPeopleInformation(List<String> peopleInformation) {
        this.peopleInformation = peopleInformation;
    }

    public TripInformation getTripInformation() {
        return tripInformation;
    }

    public void setTripInformation(TripInformation tripInformation) {
        this.tripInformation = tripInformation;
    }
}
