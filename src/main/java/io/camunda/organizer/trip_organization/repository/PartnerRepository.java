package io.camunda.organizer.trip_organization.repository;

import io.camunda.organizer.trip_organization.model.OfferType;
import io.camunda.organizer.trip_organization.model.database.Partner;
import io.camunda.organizer.trip_organization.model.TransportationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    List<Partner> findByOfferType(OfferType offerType);

    List<Partner> findByTransportationType(TransportationType transportationType);

    @Query("SELECT DISTINCT p FROM Partner p JOIN FETCH p.cities c WHERE p.transportationType = :transportationType")
    List<Partner> findByTransportationTypeWithCities(@Param("transportationType") TransportationType transportationType);

    @Query("""
    SELECT DISTINCT p FROM Partner p
    JOIN FETCH p.cities c1
    JOIN p.cities c2
    WHERE c1.id = :cityAId AND c2.id = :cityBId AND p.transportationType = :transportationType
""")
    List<Partner> findPartnersByCityIdsAndTransportWithCities(
            @Param("cityAId") Long cityAId,
            @Param("cityBId") Long cityBId,
            @Param("transportationType") TransportationType transportationType
    );

    @Query("SELECT DISTINCT p FROM Partner p JOIN FETCH p.cities c WHERE p.offerType = :offerType AND c.id = :cityId")
    List<Partner> findAccommodationInCityWithCities(
            @Param("offerType") OfferType offerType,
            @Param("cityId") Long cityId
    );

    @Query("SELECT p FROM Partner p JOIN p.cities c " +
            "WHERE p.offerType = :offerType " +
            "AND c.id = :cityId")
    List<Partner> findAccommodationInCity(
            @Param("offerType") OfferType offerType,
            @Param("cityId") Long cityId
    );

    @Query("""
                    SELECT p FROM Partner p
                    JOIN p.cities c1
                    JOIN p.cities c2
                    WHERE c1.id = :cityAId
                      AND c2.id = :cityBId
                      AND p.transportationType = :type
            """)
    List<Partner> findPartnersByCityIdsAndTransport(
            @Param("cityAId") Long cityAId,
            @Param("cityBId") Long cityBId,
            @Param("type") TransportationType type
    );
}
