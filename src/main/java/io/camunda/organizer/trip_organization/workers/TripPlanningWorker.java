package io.camunda.organizer.trip_organization.workers;

import io.camunda.organizer.trip_organization.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

import java.util.HashMap;

@Component
public class TripPlanningWorker {
    private final static Logger LOG = LoggerFactory.getLogger(TripPlanningWorker.class);

    @Autowired
    private MessageService messageService;

    @JobWorker(type = "publish_trip")
    public void publishTrip(@Variable(name = "trip_name") String tourGuide) {
        LOG.info("charging credit card: {}", tourGuide);
    }

    @JobWorker(type = "send_assign_guide")
    public void assignGuide(@Variable(name = "trip_id") String tripId) {
        LOG.info("assigning guide");
        messageService.throwMessage("assignGuideReceived", tripId, new HashMap<>());
    }

    @JobWorker(type = "send_trip")
    public void sendItinerary(@Variable(name = "trip_id") String tripId) {
        LOG.info("sending itinerary");
        messageService.throwMessage("tripItineraryReceived", tripId, new HashMap<>());
    }

    @JobWorker(type = "cancel_trip")
    public void cancelTrip(@Variable(name = "trip_id") String tripId) {
        LOG.info("cancelling everything");
    }
}