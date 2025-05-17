package io.camunda.organizer.trip_organization.service;

import io.camunda.organizer.trip_organization.model.database.*;
import io.camunda.organizer.trip_organization.model.OfferType;
import io.camunda.organizer.trip_organization.repository.CityRepository;
import io.camunda.organizer.trip_organization.repository.PartnerOfferRepository;
import io.camunda.organizer.trip_organization.repository.PartnerRepository;
import io.camunda.organizer.trip_organization.repository.TripInformationRepository;
import io.camunda.organizer.trip_organization.workers.TripApplicationWorker;
import io.camunda.organizer.trip_organization.controller.TaskListController;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CamundaService {

    @Autowired
    private ZeebeClient zeebeClient;

    @Autowired
    private PartnerOfferRepository partnerOfferRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private TripInformationRepository tripInformationRepository;

    @Autowired
    private CityRepository cityRepository;

    private final static Logger LOG = LoggerFactory.getLogger(TripApplicationWorker.class);

    public CamundaService() {
        this.zeebeClient = ZeebeClient.newClientBuilder().gatewayAddress("localhost:26500").usePlaintext().build();
    }

    @Autowired
    private TaskListController tasklistController;

    public ProcessInstanceEvent startProcess(String processId, Map<String, Object> variables) {
        ProcessInstanceEvent processInstance = zeebeClient
                .newCreateInstanceCommand()
                .bpmnProcessId(processId)
                .latestVersion()
                .variables(variables)
                .send()
                .join();

        return processInstance;
    }

    public void applyForTrip(ApplicationRequest applicationRequest, String applicationId) {
        zeebeClient.newPublishMessageCommand()
                .messageName("tripApplicationReceived")
                .correlationKey(applicationId)
                .variables(Map.of(
                        "application_id", applicationId
                ))
                .send()
                .join();
    }

    public void tripPaid(String applicationId) {
        LOG.info("paying for the trip {}", applicationId);

        zeebeClient.newPublishMessageCommand()
                .messageName("receive_payment_proof")
                .correlationKey(applicationId)
                .send()
                .join();
    }

    public void offerReceived(String partnerId, String cityId, OfferType offerType, long processKey) {
        LOG.info("offer received");
        int price = (int) ((Math.random() * (600 - 300)) + 300);

        Map<String, Object> offer = new HashMap<>();
        offer.put("partnerId", partnerId);
        offer.put("price", price);

        Map<String, Object> variables = new HashMap<>();
        variables.put("offer", offer);

        Partner partner = partnerRepository.findById(Long.parseLong(partnerId))
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        TripInformation tripInformation = tripInformationRepository.findById(processKey)
                .orElseThrow(() -> new RuntimeException("TripInformation not found"));

        City city = cityRepository.findById(Long.valueOf(cityId))
                .orElseThrow(() -> new RuntimeException("City not found"));

        PartnerOffer dbOffer = new PartnerOffer();
        dbOffer.setPartner(partner);
        dbOffer.setTripInformation(tripInformation);
        dbOffer.setCity(city);
        dbOffer.setPricePerPerson(price);
        dbOffer.setAccepted(false);

        partnerOfferRepository.save(dbOffer);

        zeebeClient.newPublishMessageCommand()
                .messageName(offerType.getMessageName())
                .correlationKey(processKey+"_"+partnerId+"_"+cityId)
                .variables(variables)
                .send();
    }
}



