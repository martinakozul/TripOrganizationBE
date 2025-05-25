package io.camunda.organizer.trip_organization.repository;

import io.camunda.organizer.trip_organization.model.database.TripCity;
import io.camunda.organizer.trip_organization.model.database.TripCityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripCityRepository extends JpaRepository<TripCity, TripCityId> {

    List<TripCity> findById_TripId(Long tripId);

    TripCity findById_TripIdAndId_CityId(Long tripId, Long cityId);
}