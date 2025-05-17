package io.camunda.organizer.trip_organization.repository;

import io.camunda.organizer.trip_organization.model.database.TripPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripItineraryRepository extends JpaRepository<TripPlan, Long> {
}
