package io.camunda.organizer.trip_organization.controller;

import io.camunda.organizer.trip_organization.model.dtos.ApplicationRequestDTO;
import io.camunda.organizer.trip_organization.model.database.ApplicationRequest;
import io.camunda.organizer.trip_organization.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trips")
public class TripApplicationController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TripService tripService;

    @Autowired
    private TripApplicationService tripApplicationService;

    @Autowired
    private TaskListController tasklistController;

    @Autowired
    private EmailService emailService;

    @Transactional
    @PostMapping("/apply")
    public Long applyForTrip(@RequestBody ApplicationRequestDTO applicationRequestDTO) {
        emailService.sendTestEmail(
                "automated.traveld@gmail.com",
                applicationRequestDTO.getEmail(),
                "Application request",
                String.format(
                        "Application request - trip ID %d\n" +
                                "Number of people %d,\n" +
                                "Names: %s\n" +
                                "Contact number: %s\n" +
                                "Email: %s",
                        applicationRequestDTO.getTripId(),
                        applicationRequestDTO.getNumberOfTravelers(),
                        String.join(", ", applicationRequestDTO.getPeopleInformation()),
                        applicationRequestDTO.getPhoneNumber(),
                        applicationRequestDTO.getEmail()
                )
        );
        ApplicationRequest applicationRequest = tripApplicationService.saveApplicationToDatabase(applicationRequestDTO);
        tripService.sendApplication(applicationRequest);
        return applicationRequest.getId();
    }
}
