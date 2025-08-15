package io.camunda.organizer.trip_organization.service;

import io.camunda.organizer.trip_organization.model.database.City;
import io.camunda.organizer.trip_organization.model.OfferType;
import io.camunda.organizer.trip_organization.model.database.Partner;
import io.camunda.organizer.trip_organization.model.TransportationType;
import io.camunda.organizer.trip_organization.repository.CityRepository;
import io.camunda.organizer.trip_organization.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PartnerService {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private CityRepository cityRepository;

    public List<Partner> findPartnersInCities(List<Long> cityIds, TransportationType transportationType, OfferType offerType) {
        Set<Partner> partnerList = new HashSet<>();

        if (offerType == OfferType.TRANSPORT) {
            if (transportationType == TransportationType.BUS) {
                partnerList.addAll(partnerRepository.findByTransportationTypeWithCities(TransportationType.BUS));
            } else {
                for (int i = 0; i < cityIds.size() - 1; i++) {
                    Long startCityId = cityIds.get(i);
                    Long destinationCityId = cityIds.get(i + 1);

                    City cityA = cityRepository.findById(startCityId)
                            .orElseThrow(() -> new RuntimeException("City A not found"));
                    City cityB = cityRepository.findById(destinationCityId)
                            .orElseThrow(() -> new RuntimeException("City B not found"));

                    List<Partner> partners = partnerRepository.findPartnersByCityIdsAndTransportWithCities(
                            cityA.getId(), cityB.getId(), TransportationType.PLANE
                    );

                    partnerList.addAll(partners);

                    if (partners.isEmpty()) {
                        System.out.printf("No plane partner between %s and %s%n", startCityId, destinationCityId);
                    } else {
                        System.out.printf("Found partner(s) for %s → %s%n", startCityId, destinationCityId);
                    }
                }
            }
        } else {
            for (Long cityId : cityIds) {
                partnerList.addAll(partnerRepository.findAccommodationInCityWithCities(OfferType.ACCOMMODATION, cityId));
            }
        }

        return new ArrayList<>(partnerList);
    }
}
