package io.camunda.organizer.trip_organization.workers;

import io.camunda.organizer.trip_organization.helper.CamundaLogHelper;
import io.camunda.organizer.trip_organization.model.OfferType;
import io.camunda.organizer.trip_organization.model.TransportationType;
import io.camunda.organizer.trip_organization.model.database.City;
import io.camunda.organizer.trip_organization.model.database.Partner;
import io.camunda.organizer.trip_organization.repository.CityRepository;
import io.camunda.organizer.trip_organization.service.EmailService;
import io.camunda.organizer.trip_organization.service.PartnerService;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class TripOffersWorker {

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CityRepository cityRepository;

    @JobWorker(type = "fetch_partners")
    public Map<String, Object> fetchPartners(
            @Variable(name = "trip_id") String tripId,
            @Variable(name = "cities") String cities,
            @Variable(name = "transportation") String transportation) {

        CamundaLogHelper.logToCsvPrep(Long.parseLong(tripId), "Fetch partners", null, "Backend");

        CamundaLogHelper.logToCsvPrep(Long.parseLong(tripId), "Gather offers", LocalDateTime.now().plusSeconds(5).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), "Backend");

        List<Long> cityIdList = Arrays.stream(cities.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .toList();

        TransportationType transportationType = transportation.equalsIgnoreCase("plane")
                ? TransportationType.PLANE
                : TransportationType.BUS;

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
    public void transportInquire(@Variable(name = "trip_id") String tripId) {
//        CamundaLogHelper.logToCsv(Long.parseLong(tripId), "Send transport inquire", null, "Backend");
    }

    @JobWorker(type = "accommodation_inquire")
    public void accommodationInquire(@Variable(name = "trip_id") String tripId) {
//        CamundaLogHelper.logToCsv(Long.parseLong(tripId), "Send accommodation inquire", null, "Backend");
    }

    @JobWorker(type = "reject_offers")
    public void rejectOffers(@Variable(name = "trip_id") String tripId) {
        CamundaLogHelper.logToCsvPrep(Long.parseLong(tripId), "Reject other offers", null, "Backend");
        emailService.sendTestEmail("",
                "automated.travels@gmail.com",
                "Offer response",
                "We thank you for your offer, but this time we have decided to go with a different provider. We hope we will be able to work together soon!");

    }

    @JobWorker(type = "reject_all_offers")
    public void rejectAllOffers(@Variable(name = "trip_id") String tripId) {
        CamundaLogHelper.logToCsvPrep(Long.parseLong(tripId), "Reject all offers", null, "Backend");
        emailService.sendTestEmail("",
                "automated.travels@gmail.com",
                "Offer response",
                "We thank you for your offer, but this time we have decided to go with a different provider. We hope we will be able to work together soon!");

    }

    @JobWorker(type = "accept_offers")
    public void acceptOffers(@Variable(name = "trip_id") String tripId) {
        CamundaLogHelper.logToCsvPrep(Long.parseLong(tripId), "Accept offers", null, "Backend");
        emailService.sendTestEmail("",
                "automated.travels@gmail.com",
                "Offer response",
                "We thank you for your offer, please reserve us the agreed upon spots.");

    }
}