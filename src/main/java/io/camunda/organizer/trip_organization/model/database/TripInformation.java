package io.camunda.organizer.trip_organization.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.camunda.organizer.trip_organization.model.TransportationType;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class TripInformation {

    @Id
    private Long id;

    private String tripName;
    private Long tripStartDate;
    private Long tripEndDate;
    private int minTravelers;
    private int maxTravelers;
    @Enumerated(EnumType.STRING)
    private TransportationType transportation;

    private double price;

    private String note;

    @ManyToOne
    @JoinColumn(name = "guide_id")
    private User guide;

    @ManyToOne
    @JoinColumn(name = "coordinator_id")
    private User coordinator;

    @ManyToOne
    @JoinColumn(name = "bus_transport_partner_id")
    private Partner transportPartner;

    @OneToMany(mappedBy = "tripInformation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ApplicationRequest> applications = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TripCity> cities = new ArrayList<>();

    public TripInformation() {
    }

    public TripInformation(String tripName, List<TripCity> cities, Long tripStartDate, Long tripEndDate, int minTravelers, int maxTravelers, TransportationType transportation) {
        this.tripName = tripName;
        this.cities = cities;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.minTravelers = minTravelers;
        this.maxTravelers = maxTravelers;
        this.transportation = transportation;
    }

    public TripInformation(Long id, String tripName, List<TripCity> cities, Long tripStartDate, Long tripEndDate, int minTravelers, int maxTravelers, TransportationType transportation, double price, String note) {
        this.id = id;
        this.tripName = tripName;
        this.cities = cities;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.minTravelers = minTravelers;
        this.maxTravelers = maxTravelers;
        this.transportation = transportation;
        this.price = price;
        this.note = note;
    }

    public List<TripCity> getCities() {
        return cities;
    }

    public void setCities(List<TripCity> cities) {
        this.cities = cities;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public Long getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartDate(Long tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public Long getTripEndDate() {
        return tripEndDate;
    }

    public void setTripEndDate(Long tripEndDate) {
        this.tripEndDate = tripEndDate;
    }

    public int getMinTravelers() {
        return minTravelers;
    }

    public void setMinTravelers(int minTravelers) {
        this.minTravelers = minTravelers;
    }

    public int getMaxTravelers() {
        return maxTravelers;
    }

    public void setMaxTravelers(int maxTravelers) {
        this.maxTravelers = maxTravelers;
    }

    public TransportationType getTransportation() {
        return transportation;
    }

    public void setTransportation(TransportationType transportation) {
        this.transportation = transportation;
    }

    public User getGuide() {
        return guide;
    }

    public void setGuide(User guide) {
        this.guide = guide;
    }

    public User getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(User coordinator) {
        this.coordinator = coordinator;
    }

    public List<ApplicationRequest> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationRequest> applications) {
        this.applications = applications;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}