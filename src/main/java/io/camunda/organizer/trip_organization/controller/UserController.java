package io.camunda.organizer.trip_organization.controller;

import io.camunda.organizer.trip_organization.model.dtos.BasicTrip;
import io.camunda.organizer.trip_organization.model.database.TripInformation;
import io.camunda.organizer.trip_organization.model.dtos.UserDto;
import io.camunda.organizer.trip_organization.model.Role;
import io.camunda.organizer.trip_organization.model.database.User;
import io.camunda.organizer.trip_organization.repository.TripInformationRepository;
import io.camunda.organizer.trip_organization.repository.UserRepository;
import io.camunda.organizer.trip_organization.service.TripService;
import io.camunda.organizer.trip_organization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripService tripService;

    @Autowired
    private UserService userService;

    @Autowired
    private TripInformationRepository tripInformationRepository;

    @Autowired
    private TaskListController tasklistController;

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody UserDto user) {
        return ResponseEntity.ok(userService.saveNewUser(user));
    }

    @GetMapping("/{userId}/trips")
    public ResponseEntity<List<TripInformation>> getTripsForUser(@PathVariable String userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (user.get().getRole() == Role.COORDINATOR) {
            List<TripInformation> trips = tripInformationRepository.findByCoordinator(user.get());
            return ResponseEntity.ok(trips);
        } else {
            List<TripInformation> trips = tripInformationRepository.findByGuide(user.get());
            return ResponseEntity.ok(trips);
        }
    }

    @GetMapping("/availableGuides")
    public ResponseEntity<List<String>> getAvailableGuides(@RequestParam Long processKey) {
        return ResponseEntity.ok(tripService.getAvailableGuides(processKey));
    }

    @GetMapping("/{userId}/tasks/all")
    public ResponseEntity<Map<String, List<BasicTrip>>> getActiveTasksForUser(@PathVariable String userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String username = userOptional.get().getUsername();
        List<BasicTrip> activeTasks = tasklistController.getActiveTasksForUser(username);

        Map<String, List<BasicTrip>> groupedByTripName = activeTasks.stream()
                .peek(task -> {
                    Optional<TripInformation> tripOptional = tripInformationRepository.findById(task.getTripId());
                    tripOptional.ifPresent(trip -> task.setTripName(trip.getTripName()));
                })
                .collect(Collectors.groupingBy(BasicTrip::getTaskName));

        return ResponseEntity.ok(groupedByTripName);
    }
}

