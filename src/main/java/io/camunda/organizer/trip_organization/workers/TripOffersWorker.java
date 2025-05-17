package io.camunda.organizer.trip_organization.workers;

import io.camunda.organizer.trip_organization.model.OfferType;
import io.camunda.organizer.trip_organization.model.TransportationType;
import io.camunda.organizer.trip_organization.model.database.City;
import io.camunda.organizer.trip_organization.model.database.Partner;
import io.camunda.organizer.trip_organization.repository.CityRepository;
import io.camunda.organizer.trip_organization.service.PartnerService;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class TripOffersWorker {

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private CityRepository cityRepository;

    private final static Logger LOG = LoggerFactory.getLogger(TripApplicationWorker.class);

    @JobWorker(type = "fetch_partners")
    public Map<String, Object> fetchPartners(
            @Variable(name = "cities") String cities,
            @Variable(name = "transportation") String transportation) {

        List<Long> cityIdList = Arrays.stream(cities.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .toList();

        TransportationType transportationType = transportation.equalsIgnoreCase("plane")
                ? TransportationType.PLANE
                : TransportationType.BUS;

        LOG.info("Cities: " + cities + ", Transportation: " + transportationType);

        List<Partner> transportPartners = partnerService.findPartnersInCities(cityIdList, transportationType, OfferType.TRANSPORT);
        List<Partner> accommodationPartners = partnerService.findPartnersInCities(cityIdList, null, OfferType.ACCOMMODATION);

        Map<Long, String> cityIdToName = cityRepository.findAllById(cityIdList).stream()
                .collect(Collectors.toMap(City::getId, City::getName));

        Function<List<Partner>, List<String>> formatPartners = partners -> partners.stream()
                .flatMap(partner ->
                        partner.getCities().stream()
                                .filter(city -> cityIdList.contains(city.getId()))
                                .map(city -> String.format("%d_%d", partner.getId(), city.getId()))
                )
                .toList();

        return Map.of(
                "transport_partner_ids", formatPartners.apply(transportPartners),
                "accommodation_partner_ids", formatPartners.apply(accommodationPartners)
        );
    }



    @JobWorker(type = "transport_inquire")
    public void transportInquire() {
        LOG.info("transport inquire");
    }

    @JobWorker(type = "accommodation_inquire")
    public void accommodationInquire() {
        LOG.info("accommodation_inquire");
    }

    @JobWorker(type = "reject_offers")
    public void rejectOffers() {
        LOG.info("rejecting offers");
    }

    @JobWorker(type = "accept_offers")
    public void acceptOffers() {
        LOG.info("accept offer");
    }
}