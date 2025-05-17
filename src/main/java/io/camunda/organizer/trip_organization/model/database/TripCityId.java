package io.camunda.organizer.trip_organization.model.database;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TripCityId implements Serializable {

    private Long tripId;
    private Long cityId;

    public TripCityId() {
    }

    public TripCityId(Long tripId, Long cityId) {
        this.tripId = tripId;
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripCityId that = (TripCityId) o;
        return Objects.equals(tripId, that.tripId) && Objects.equals(cityId, that.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tripId, cityId);
    }
}
