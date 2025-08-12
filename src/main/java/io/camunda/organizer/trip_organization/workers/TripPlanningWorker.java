package io.camunda.organizer.trip_organization.workers;

import io.camunda.organizer.trip_organization.helper.CamundaLogHelper;
import io.camunda.organizer.trip_organization.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

import java.util.HashMap;

@Component
public class TripPlanningWorker {

    @Autowired
    private MessageService messageService;

    @JobWorker(type = "publish_trip")
    public void publishTrip(@Variable(name = "trip_id") String tripId, @Variable(name = "trip_name") String tourGuide) {
        CamundaLogHelper.logToCsvPrep(Long.parseLong(tripId), "Publish trip", null, "Backend");

    }

    @JobWorker(type = "send_assign_guide")
    public void assignGuide(@Variable(name = "trip_id") String tripId) {
        messageService.throwMessage("assignGuideReceived", tripId, new HashMap<>());
    }

    @JobWorker(type = "send_trip")
    public void sendItinerary(@Variable(name = "trip_id") String tripId) {
        messageService.throwMessage("tripItineraryReceived", tripId, new HashMap<>());
    }

    @JobWorker(type = "cancel_trip")
    public void cancelTrip(@Variable(name = "trip_id") String tripId) {
    }

    @JobWorker(type = "app_lock_date")
    public void getApplicationsLockDate(@Variable(name = "trip_id") String tripId) {
        CamundaLogHelper.logToCsvPrep(Long.parseLong(tripId), "Get applications lock date", null, "Backend");
    }
}