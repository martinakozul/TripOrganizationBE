package io.camunda.organizer.trip_organization.controller;

import io.camunda.organizer.trip_organization.model.database.City;
import io.camunda.organizer.trip_organization.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @GetMapping("/all")
    public ResponseEntity<List<City>> getTripItinerary() {
        return ResponseEntity.ok(cityRepository.findAll());
    }
}
