package io.camunda.organizer.trip_organization.service;

import io.camunda.organizer.trip_organization.model.dtos.ApplicationRequestDTO;
import io.camunda.organizer.trip_organization.model.database.TripInformation;
import io.camunda.organizer.trip_organization.model.database.ApplicationRequest;
import io.camunda.organizer.trip_organization.repository.TripApplicationRepository;
import io.camunda.organizer.trip_organization.repository.TripInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripApplicationService {

    @Autowired
    private TripInformationRepository tripInformationRepository;

    @Autowired
    private TripApplicationRepository tripApplicationRepository;

    public ApplicationRequest saveApplicationToDatabase(ApplicationRequestDTO applicationRequestDTO) {
        TripInformation trip = tripInformationRepository.findById(applicationRequestDTO.getTripId()).orElseThrow();
        ApplicationRequest applicationRequest = new ApplicationRequest(applicationRequestDTO);
        applicationRequest.setTripInformation(trip);
        ApplicationRequest savedApplication = tripApplicationRepository.save(applicationRequest);
        List<ApplicationRequest> applications = trip.getApplications();
        applications.add(savedApplication);
        trip.setApplications(applications);
        tripInformationRepository.save(trip);
        return savedApplication;
    }
}
