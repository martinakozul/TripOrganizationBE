package io.camunda.organizer.trip_organization.service;

import io.camunda.organizer.trip_organization.model.dtos.ApplicationRequestDTO;
import io.camunda.organizer.trip_organization.model.database.TripInformation;
import io.camunda.organizer.trip_organization.model.database.ApplicationRequest;
import io.camunda.organizer.trip_organization.repository.TripApplicationRepository;
import io.camunda.organizer.trip_organization.repository.TripInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TripApplicationService {

    @Autowired
    private TripInformationRepository tripInformationRepository;

    @Autowired
    private TripApplicationRepository tripApplicationRepository;

    public void fillTripData() {

    }

    public ApplicationRequest saveApplicationToDatabase(ApplicationRequestDTO applicationRequestDTO) {
        TripInformation trip = tripInformationRepository
                .findById(applicationRequestDTO.getTripId())
                .orElseThrow();

        List<ApplicationRequest> applications = trip.getApplications();

        ApplicationRequest applicationRequest = new ApplicationRequest(applicationRequestDTO);
        applicationRequest.setTripInformation(trip);

        ApplicationRequest savedApplication = tripApplicationRepository.save(applicationRequest);

        applications.add(savedApplication);
        trip.setApplications(applications);
        tripInformationRepository.save(trip);

        return savedApplication;
    }

    @Transactional
    public boolean deleteIfTripFull(Long applicationId) {
        Optional<ApplicationRequest> appOpt = tripApplicationRepository.findById(applicationId);
        if (appOpt.isEmpty()) {
            return false;
        }

        ApplicationRequest application = appOpt.get();
        Long tripId = application.getTripInformation().getId();

        TripInformation trip = tripInformationRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("Trip not found"));

        long count = tripApplicationRepository.countByTripInformation_Id(tripId);

        if (count >= trip.getMaxTravelers()) {
            tripApplicationRepository.deleteById(applicationId);
            return true;
        }

        return false;
    }

    public boolean hasEnoughApplications(Long tripId) {
        TripInformation trip = tripInformationRepository.findById(tripId)
                .orElseThrow(() -> new IllegalStateException("Trip not found"));

        long count = tripApplicationRepository.countByTripInformation_Id(tripId);

        return count >= trip.getMinTravelers();
    }

    @Transactional
    public void markAsPaid(Long applicationId) {
        ApplicationRequest app = tripApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application not found"));
        app.setPaid(true);
    }

    @Transactional
    public void deleteExpiredApplication(Long applicationId) {
        tripApplicationRepository.deleteById(applicationId);
    }
}
