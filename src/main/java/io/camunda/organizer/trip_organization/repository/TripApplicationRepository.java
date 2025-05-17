package io.camunda.organizer.trip_organization.repository;

import io.camunda.organizer.trip_organization.model.database.ApplicationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripApplicationRepository extends JpaRepository<ApplicationRequest, Long> {
}
