package io.camunda.organizer.trip_organization.controller;

import io.camunda.organizer.trip_organization.helper.CamundaLogHelper;
import io.camunda.organizer.trip_organization.model.database.*;
import io.camunda.organizer.trip_organization.model.dtos.TripCityDTO;
import io.camunda.organizer.trip_organization.model.dtos.TripInformationDto;
import io.camunda.organizer.trip_organization.repository.TripCityRepository;
import io.camunda.organizer.trip_organization.repository.TripInformationRepository;
import io.camunda.organizer.trip_organization.repository.UserRepository;
import io.camunda.organizer.trip_organization.service.CamundaService;
import io.camunda.organizer.trip_organization.service.TripService;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/trip")
public class ApplicationController {

    @Autowired
    private ZeebeClient zeebeClient;

    @Autowired
    private CamundaService camundaService;

    @Autowired
    private TripService tripService;

    @Autowired
    private TaskListController tasklistController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripCityRepository tripCityRepository;

    @Autowired
    private TripInformationRepository tripInformationRepository;

    @PostMapping("/create")
    public ResponseEntity<Long> createTrip(@RequestParam String coordinatorId) {
        Optional<User> user = userRepository.findById(coordinatorId);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProcessInstanceEvent processInstanceEvent = camundaService.startProcess(
                "trip_prep_coordinator",
                Map.of("trip_coordinator", user.get().getUsername())
        );

        System.out.println("Response: " + processInstanceEvent.getProcessDefinitionKey() + " " + processInstanceEvent.getProcessInstanceKey());
        CamundaLogHelper.logToCsvPrep(processInstanceEvent.getProcessInstanceKey(), "Start create trip", null, "Coordinator");
        return ResponseEntity.ok(processInstanceEvent.getProcessInstanceKey());
    }

    @Transactional
    @PostMapping("/{processInstanceKey}/fillTripData")
    public void fillTripData(@PathVariable long processInstanceKey, @RequestBody TripInformationDto tripRequest, @RequestParam Boolean isGuide) {
        tripService.createTripWithCities(tripRequest);
        List<TripCityDTO> cityList = tripCityRepository.findById_TripId(processInstanceKey).stream().map(tc -> new TripCityDTO(tc.getCity().getId(), tc.getDaysSpent())).toList();
        TripInformationDto tripInformationDto = tripService.getTrip(processInstanceKey);
        tripInformationDto.setCities(cityList);
        tasklistController.completeTripCreation(tripInformationDto, processInstanceKey);

        if (!isGuide) {
            CamundaLogHelper.logToCsvPrep(processInstanceKey, "Create base trip", null, "Coordinator");
        } else {
            CamundaLogHelper.logToCsvPrep(processInstanceKey, "Fill trip itinerary", null, "Guide");
        }
    }

    @GetMapping("/{processInstanceKey}")
    public ResponseEntity<TripInformationDto> getTripInformation(@PathVariable long processInstanceKey) {
        return ResponseEntity.ok(tripService.getTrip(processInstanceKey));
    }

    @PostMapping("/{processInstanceKey}/saveTripData")
    public ResponseEntity<String> saveTripData(@PathVariable long processInstanceKey, @RequestBody TripInformationDto tripRequest) {
        tripService.createTripWithCities(tripRequest);
        return ResponseEntity.ok("");
    }

    @PostMapping("/{processInstanceKey}/assignTourGuide")
    public ResponseEntity<String> assignTourGuide(@PathVariable long processInstanceKey, @RequestParam String tourGuide) {
        tasklistController.assignTourGuide(tourGuide, processInstanceKey);
        CamundaLogHelper.logToCsvPrep(processInstanceKey, "Assign tour guide", null, "Coordinator");
        return ResponseEntity.ok("");
    }

    @PostMapping("/{processInstanceKey}/fillTripPlan")
    public ResponseEntity<String> fillTripPlan(@PathVariable long processInstanceKey, @RequestBody List<TripCityDTO> tripPlan) {
        String taskId = tasklistController.getTaskId(processInstanceKey);
        //TODO fix list
        tasklistController.fillTripPlan(tripPlan.get(0), taskId);
        tripService.updateItinerary(processInstanceKey, tripPlan);

        return ResponseEntity.ok("");
    }

//    @PostMapping("/{processInstanceKey}/fillTripPlan")
//    public ResponseEntity<String> fillTripPlan(@PathVariable long processInstanceKey, @RequestBody TripPlan tripPlan) {
//        String taskId = tasklistController.getTaskId(processInstanceKey);
//        tasklistController.fillTripPlan(tripPlan, taskId);
//        tripPlan.setId(processInstanceKey);
//        tripService.saveItinerary(tripPlan);
//        return ResponseEntity.ok("");
//    }

//    @GetMapping("/{processInstanceKey}/tripItinerary")
//    public ResponseEntity<List<TripCityDTO>> getTripItinerary(@PathVariable long processInstanceKey) {
//        List<TripCityDTO> cityList = tripCityRepository.findById_TripId(processInstanceKey).stream().map(tc -> new TripCityDTO(tc.getCity().getId(), tc.getDaysSpent(), tc.getCity().getName(), tc.getOrderInTrip(), tc.getPlan())).toList();
//        return ResponseEntity.ok(cityList);
//    }

    @PostMapping("/{processInstanceKey}/reviewTripItinerary")
    public ResponseEntity<String> reviewTripItinerary(@PathVariable long processInstanceKey, @RequestParam(required = false) Double price, @RequestParam(required = false) String note) {
        tasklistController.reviewItinerary(processInstanceKey, price, note);
        if (note != null) {
            tripService.updateNote(processInstanceKey, note);
        }
        if (price != null) {
            tripService.updatePrice(processInstanceKey, price);
        }
        CamundaLogHelper.logToCsvPrep(processInstanceKey, "Review trip itinerary", null, "Coordinator");
        return ResponseEntity.ok("");
    }

    @PostMapping("/getTaskId/{processInstanceKey}")
    public ResponseEntity<String> getTaskId(@PathVariable long processInstanceKey) {
        String taskId = tasklistController.getTaskId(processInstanceKey);
        return ResponseEntity.ok(taskId);
    }

}
