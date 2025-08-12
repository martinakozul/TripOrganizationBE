package io.camunda.organizer.trip_organization.workers;

import io.camunda.organizer.trip_organization.controller.ApplicationController;
import io.camunda.organizer.trip_organization.controller.TripApplicationController;
import io.camunda.organizer.trip_organization.controller.TripOffersController;
import io.camunda.organizer.trip_organization.helper.CamundaLogHelper;
import io.camunda.organizer.trip_organization.model.TransportationType;
import io.camunda.organizer.trip_organization.model.dtos.ApplicationRequestDTO;
import io.camunda.organizer.trip_organization.model.dtos.TripCityDTO;
import io.camunda.organizer.trip_organization.model.dtos.TripInformationDto;
import io.camunda.organizer.trip_organization.service.CamundaService;
import io.camunda.organizer.trip_organization.service.MessageService;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class GeneratorWorker {

    @Autowired
    private CamundaService camundaService;

    @Autowired
    private ApplicationController applicationController;

    @Autowired
    private TripOffersController tripOffersController;

    @Autowired
    private TripApplicationController tripApplicationController;

    @Autowired
    MessageService messageService;

    @JobWorker(type = "mock_trip")
    public void mockTrip() {
        Long processKey = applicationController.createTrip("auth0|683313efdd9b820049930483").getBody();

        assert processKey != null;
        messageService.throwMessage(
                "mock_start",
                String.valueOf(processKey),
                Map.of("tripId", processKey)
        );
    }

    @JobWorker(type = "mock_trip_data")
    public void mockTripData(@Variable(name = "tripId") String tripId) {
        List<TripCityDTO> tripCities = new ArrayList<>();

        tripCities.add(new TripCityDTO(5L, 3));
        tripCities.add(new TripCityDTO(7L, 2));

        applicationController.fillTripData(Long.parseLong(tripId), new TripInformationDto(
                Long.parseLong(tripId),
                "Trip name",
                1753176798000L,
                1753176798000L,
                10,
                30,
                TransportationType.BUS,
                "auth0|683313efdd9b820049930483",
                tripCities
        ), false);
    }

    @JobWorker(type = "mock_assign_guide")
    public Map<String, Boolean> mockAssignGuide(@Variable(name = "tripId") String tripId) {
        Random random = new Random();

        int nextInt = random.nextInt(10);

        String guide = null;
        if (false) {
            guide = "guide1@gmail.com";
        }

        applicationController.assignTourGuide(Long.parseLong(tripId), guide);

        return Map.of(
                "guideAssigned", false
        );
    }

    @JobWorker(type = "mock_partner_offers")
    public void mockPartnerOffers(@Variable(name = "tripId") String tripId) {
        long tripIdLong = Long.parseLong(tripId);
        tripOffersController.receiveAccommodationOffer("1", "7", tripIdLong);
        tripOffersController.receiveAccommodationOffer("1", "5", tripIdLong);
        tripOffersController.receiveAccommodationOffer("10", "5", tripIdLong);

        tripOffersController.receiveTransportOffer("13", "5", tripIdLong);
        tripOffersController.receiveTransportOffer("2", "5", tripIdLong);
        tripOffersController.receiveTransportOffer("11", "7", tripIdLong);
    }

    @JobWorker(type = "mock_review_offers")
    public Map<String, Boolean> mockAcceptOffers(@Variable(name = "tripId") String tripId) {
        Random random = new Random();

        int nextInt = random.nextInt(10);

        if (nextInt == 0) {
            tripOffersController.rejectAllOffersForTrip(Long.parseLong(tripId));
            return Map.of(
                    "offerRejected", true
            );
        }
        List<Long> transportPartner = new ArrayList<>();
        List<Long> accommodationPartner = new ArrayList<>();

        transportPartner.add(3L);
        accommodationPartner.add(1L);
        accommodationPartner.add(10L);

        tripOffersController.acceptOffersForTrip(Long.parseLong(tripId), transportPartner, accommodationPartner);

        return Map.of(
                "offerRejected", false
        );
    }

    @JobWorker(type = "mock_define_itinerary")
    public void mockDefineItinerary(@Variable(name = "tripId") String tripId) {
        List<TripCityDTO> tripCities = new ArrayList<>();

        tripCities.add(new TripCityDTO(
                5L,
                3,
                "Zagreb",
                1,
                "Relax and explore museums",
                Arrays.asList("Museum of Broken Relationships", "Dolac Market"),
                List.of("Day trip to Samobor")
        ));

        tripCities.add(new TripCityDTO(
                7L,
                2,
                "Split",
                2,
                "Beach and old town tour",
                Arrays.asList("Diocletian’s Palace", "Marjan Hill"),
                List.of("Island hopping")
        ));


        applicationController.fillTripData(Long.parseLong(tripId), new TripInformationDto(
                Long.parseLong(tripId),
                "Trip name",
                1753176798000L,
                1753176798000L,
                10,
                30,
                TransportationType.BUS,
                "auth0|687f411afb6744d5fe3ca971",
                tripCities
        ), true);
    }

    @JobWorker(type = "mock_review_itinerary")
    public Map<String, Boolean> mockReviewItinerary(@Variable(name = "tripId") String tripId) {
        Random random = new Random();

        int nextInt = random.nextInt(5);

        if (nextInt == 0) {
            applicationController.reviewTripItinerary(Long.parseLong(tripId), null, "Note");
        } else {
            applicationController.reviewTripItinerary(Long.parseLong(tripId), 300.0, null);
        }

        return Map.of(
                "changesRequested", nextInt == 0
        );
    }

    @JobWorker(type = "mock_task_durations")
    public Map<String, String> mockTaskDuration(@Variable(name = "isItineraryTask") Boolean isItineraryTask) {
        Map<String, String> variables = new HashMap<>();

        if (!isItineraryTask) {
            variables.put("tripDataDuration", randomDurationString(30, 40));
            variables.put("assignGuideDuration", randomDurationString(30, 40));
            variables.put("partnerOfferDuration", randomDurationString(30, 40));
            variables.put("reviewPartnerOfferDuration", randomDurationString(30, 300));
        } else {
            variables.put("tripItineraryDuration", randomDurationString(30, 300));
            variables.put("reviewTripItineraryDuration", randomDurationString(300, 600));
        }

        return variables;
    }

    @JobWorker(type = "mock_apply")
    public void mockApplication(@Variable(name = "tripId") String tripId) {
        List<String> peopleInformation = new ArrayList<>();
        peopleInformation.add("Martina Kožul");
        peopleInformation.add("Marin Maršić");
        Long applicationId = tripApplicationController.applyForTrip(new ApplicationRequestDTO(
                "tinakozul02@gmail.com",
                "+385 98 933 6129",
                2,
                peopleInformation,
                Long.parseLong(tripId)
        ));

        CamundaLogHelper.logToCsvApplication(applicationId, "Apply to trip", null, "User");
        messageService.throwMessage(
                "mock_apply_start",
                String.valueOf(applicationId),
                Map.of("applicationId", applicationId)
        );
    }

    @JobWorker(type = "mock_send_payment_proof")
    public void mockPaymentProof(@Variable(name = "applicationId") Long applicationId) {
        CamundaLogHelper.logToCsvApplication(applicationId, "Payment proof received", null, "User");
        camundaService.tripPaid(String.valueOf(applicationId));
    }

    @JobWorker(type = "mock_send_cancel_application")
    public void mockCancelApplication(@Variable(name = "applicationId") Long applicationId) {
        CamundaLogHelper.logToCsvApplication(applicationId, "Cancel application request received", null, "User");
        messageService.throwMessage(
                "receive_cancel_application",
                String.valueOf(applicationId),
                Map.of()

        );
    }

    @JobWorker(type = "mock_application_process")
    public Map<String, String> mock_application_process(@Variable(name = "applicationId") Long applicationId) {
        Map<String, String> variables = new HashMap<>();

        Random random = new Random();

        if (random.nextInt(5) != 0) {
            variables.put("paymentDuration", randomDurationString(50, 240));
        } else {
            variables.put("cancelDuration", null);
        }
        if (random.nextInt(5) == 0) {
            variables.put("cancelDuration", randomDurationString(60, 300));
        } else {
            variables.put("cancelDuration", null);
        }

        return variables;
    }

    private String randomDurationString(int minSeconds, int maxSeconds) {
        int seconds = ThreadLocalRandom.current().nextInt(minSeconds, maxSeconds + 1);
        return Duration.ofSeconds(seconds).toString();
    }
}
