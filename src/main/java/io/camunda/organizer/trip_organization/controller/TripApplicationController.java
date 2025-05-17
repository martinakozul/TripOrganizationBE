package io.camunda.organizer.trip_organization.controller;

import io.camunda.organizer.trip_organization.model.dtos.ApplicationRequestDTO;
import io.camunda.organizer.trip_organization.model.database.ApplicationRequest;
import io.camunda.organizer.trip_organization.service.CamundaService;
import io.camunda.organizer.trip_organization.service.MessageService;
import io.camunda.organizer.trip_organization.service.TripApplicationService;
import io.camunda.organizer.trip_organization.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trips")
public class TripApplicationController {

    @Autowired
    private CamundaService camundaService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private TripService tripService;

    @Autowired
    private TripApplicationService tripApplicationService;

    @Autowired
    private TaskListController tasklistController;

    @PostMapping("/apply")
    public String applyForTrip(@RequestBody ApplicationRequestDTO applicationRequestDTO) throws InterruptedException {
        ApplicationRequest applicationRequest = tripApplicationService.saveApplicationToDatabase(applicationRequestDTO);
        tripService.sendApplication(applicationRequest);
        return "";
    }

//    @PostMapping("/fillApplication")
//    public String fillApplication(@RequestBody ApplicationRequest request, @RequestParam long processInstanceKey) {
//        String taskId = tasklistController.getTaskId(processInstanceKey);
//        tasklistController.applyForTheTrip(request, taskId);
//        return "";
//    }

    @PostMapping("/pay")
    public String payForTheTrip(@RequestParam String applicationId) {
        camundaService.tripPaid(
                applicationId
        );
        return applicationId;
    }
}
