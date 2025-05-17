package io.camunda.organizer.trip_organization.repository;

import io.camunda.organizer.trip_organization.model.database.PartnerOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerOfferRepository extends JpaRepository<PartnerOffer, Long> {

    List<PartnerOffer> findByTripInformation_Id(Long tripInformationId);

}
