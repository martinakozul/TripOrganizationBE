package io.camunda.organizer.trip_organization.service;

import io.camunda.organizer.trip_organization.model.dtos.NamedPartnerOffer;
import io.camunda.organizer.trip_organization.model.database.Partner;
import io.camunda.organizer.trip_organization.model.database.PartnerOffer;
import io.camunda.organizer.trip_organization.repository.PartnerOfferRepository;
import io.camunda.organizer.trip_organization.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TripOfferService {

    @Autowired
    private PartnerOfferRepository offerRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    public Map<String, List<NamedPartnerOffer>> getOfferForTrip(long tripId) {
        List<PartnerOffer> offers = offerRepository.findByTripInformation_Id(tripId);

        if (offers.isEmpty()) {
            return new HashMap<>();
        }
        Set<Long> partnerIds = offers.stream()
                .map(po -> po.getPartner().getId())
                .collect(Collectors.toSet());

        List<Partner> partners = partnerRepository.findAllById(partnerIds);

        Map<Long, Partner> partnerMap = partners.stream()
                .collect(Collectors.toMap(Partner::getId, Function.identity()));

        return offers.stream()
                .map(o -> {
                    Partner partner = partnerMap.get(o.getPartner().getId());
                    return new AbstractMap.SimpleEntry<>(
                            partner.getOfferType().name().toLowerCase(),
                            new NamedPartnerOffer(o.getId(), o.getPartner().getId(), o.getTripInformation().getId(), partner.getName(), o.getPricePerPerson(), o.getCity().getId(), o.getCity().getName())
                    );
                })
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
    }
}
