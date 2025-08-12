package io.camunda.organizer.trip_organization.controller;

import io.camunda.organizer.trip_organization.helper.CamundaLogHelper;
import io.camunda.organizer.trip_organization.model.database.Partner;
import io.camunda.organizer.trip_organization.model.dtos.NamedPartnerOffer;
import io.camunda.organizer.trip_organization.model.OfferType;
import io.camunda.organizer.trip_organization.service.CamundaService;
import io.camunda.organizer.trip_organization.service.EmailService;
import io.camunda.organizer.trip_organization.service.PartnerService;
import io.camunda.organizer.trip_organization.service.TripOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/offer")
public class TripOffersController {

    @Autowired
    private CamundaService camundaService;

    @Autowired
    private TripOfferService offerService;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private TaskListController tasklistController;

    @GetMapping
    public ResponseEntity<List<Partner>> test() {
        return ResponseEntity.ok(partnerService.findPartnersInCities(List.of(5L, 4L, 5L), null, OfferType.ACCOMMODATION));
    }

    @PostMapping("/transport")
    public void receiveTransportOffer(@RequestParam String partnerId, @RequestParam String cityId, @RequestParam long processKey) {
        camundaService.offerReceived(partnerId, cityId, OfferType.TRANSPORT, processKey);
    }

    @PostMapping("/accommodation")
    public void receiveAccommodationOffer(@RequestParam String partnerId, @RequestParam String cityId, @RequestParam long processKey) {
        camundaService.offerReceived(partnerId, cityId, OfferType.ACCOMMODATION, processKey);
    }

    @GetMapping("trip/{processKey}")
    public ResponseEntity<Map<String, List<NamedPartnerOffer>>> getOffers(@PathVariable Long processKey) {
        return ResponseEntity.ok(offerService.getOfferForTrip(processKey));
    }

    @PostMapping("trip/{processKey}/rejectAll")
    public void rejectAllOffersForTrip(@PathVariable Long processKey) {
        String taskId = tasklistController.getTaskId(processKey);
        tasklistController.reviewPartnerOffers(null, null, taskId);
        CamundaLogHelper.logToCsvPrep(processKey, "Review partner offers", null, "Coordinator");
    }

    @PostMapping("trip/{processKey}/accept")
    public void acceptOffersForTrip(@PathVariable Long processKey, @RequestParam List<Long> transportPartnerIds, @RequestParam List<Long> accommodationPartnerIds) {
        String taskId = tasklistController.getTaskId(processKey);
        tasklistController.reviewPartnerOffers(transportPartnerIds, accommodationPartnerIds, taskId);
        CamundaLogHelper.logToCsvPrep(processKey, "Review partner offers", null, "Coordinator");
    }

    @Autowired
    private EmailService emailService;
}
