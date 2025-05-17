package io.camunda.organizer.trip_organization.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

@Component
public class TripPlanningWorker {
    private final static Logger LOG = LoggerFactory.getLogger(TripPlanningWorker.class);
    @JobWorker(type = "publish_trip")
    public void publishTrip(@Variable(name = "trip_name") String tourGuide) {
        LOG.info("charging credit card: {}", tourGuide);
    }
}