package io.camunda.organizer.trip_organization.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class TripCity {

    @EmbeddedId
    private TripCityId id = new TripCityId();

    @ManyToOne
    @MapsId("tripId")
    @JsonIgnore
    private TripInformation trip;

    @ManyToOne
    @MapsId("cityId")
    @JsonIgnore
    private City city;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Partner accommodation;

    @ManyToOne
    @JoinColumn(name = "transport_id", nullable = true)
    private Partner transport;

    private int daysSpent;
    private int orderInTrip;
    private String plan;
    private List<String> includedActivities;
    private List<String> extraActivities;

    public TripCity() {
    }

    public TripCity(TripCityId id, TripInformation trip, City city, int daysSpent, int orderInTrip) {
        this.id = id;
        this.trip = trip;
        this.city = city;
        this.daysSpent = daysSpent;
        this.orderInTrip = orderInTrip;
    }

    public TripCity(TripCityId id, TripInformation trip, City city, int daysSpent, int orderInTrip, String plan, List<String> includedActivities, List<String> extraActivities) {
        this.id = id;
        this.trip = trip;
        this.city = city;
        this.daysSpent = daysSpent;
        this.orderInTrip = orderInTrip;
        this.plan = plan;
        this.includedActivities = includedActivities;
        this.extraActivities = extraActivities;
    }

    public List<String> getIncludedActivities() {
        return includedActivities;
    }

    public void setIncludedActivities(List<String> includedActivities) {
        this.includedActivities = includedActivities;
    }

    public List<String> getExtraActivities() {
        return extraActivities;
    }

    public void setExtraActivities(List<String> extraActivities) {
        this.extraActivities = extraActivities;
    }

    public TripCityId getId() {
        return id;
    }

    public void setId(TripCityId id) {
        this.id = id;
    }

    public TripInformation getTrip() {
        return trip;
    }

    public void setTrip(TripInformation trip) {
        this.trip = trip;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public int getDaysSpent() {
        return daysSpent;
    }

    public void setDaysSpent(int daysSpent) {
        this.daysSpent = daysSpent;
    }

    public int getOrderInTrip() {
        return orderInTrip;
    }

    public void setOrderInTrip(int orderInTrip) {
        this.orderInTrip = orderInTrip;
    }
}

