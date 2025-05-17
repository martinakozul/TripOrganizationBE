package io.camunda.organizer.trip_organization.repository;

import io.camunda.organizer.trip_organization.model.database.TripInformation;
import io.camunda.organizer.trip_organization.model.database.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripInformationRepository extends JpaRepository<TripInformation, Long> {

    List<TripInformation> findByGuide(User guide);

    List<TripInformation> findByCoordinator(User coordinator);

    List<TripInformation> findByTripStartDateLessThanAndTripEndDateGreaterThan(long end, long start);

    @Modifying
    @Query("UPDATE TripInformation ti SET ti.price = :price WHERE ti.id = :id")
    void updatePriceById(@Param("id") Long id, @Param("price") Double price);

    @Modifying
    @Query("UPDATE TripInformation ti SET ti.note = :note WHERE ti.id = :id")
    void updateNoteById(@Param("id") Long id, @Param("note") String note);

}
