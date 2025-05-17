package io.camunda.organizer.trip_organization.repository;

import io.camunda.organizer.trip_organization.model.database.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByPartnersName(String partnerName);

    Optional<City> findByName(String name);
}
