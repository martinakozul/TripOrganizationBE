package io.camunda.organizer.trip_organization.service;

import io.camunda.organizer.trip_organization.model.database.*;
import io.camunda.organizer.trip_organization.model.Role;
import io.camunda.organizer.trip_organization.model.dtos.TripCityDTO;
import io.camunda.organizer.trip_organization.model.dtos.TripInformationDto;
import io.camunda.organizer.trip_organization.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TripService {

    @Autowired
    MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TripCityRepository tripCityRepository;

    @Autowired
    private TripItineraryRepository tripItineraryRepository;

    @Autowired
    private TripInformationRepository tripInformationRepository;

    public List<String> getAvailableGuides(long processKey) {
        TripInformation trip = tripInformationRepository.findById(processKey).get();

        List<TripInformation> tripsInRange = tripInformationRepository.findByTripStartDateLessThanAndTripEndDateGreaterThan(trip.getTripEndDate(), trip.getTripStartDate());

        Set<Long> unavailableGuideIds = new HashSet<>();
        for (TripInformation t : tripsInRange) {
            if (t.getGuide() != null) {
                unavailableGuideIds.add(t.getGuide().getId());
            }
        }

        List<User> allGuides = userRepository.findByRole(Role.GUIDE);

        return allGuides.stream()
                .filter(guide -> !unavailableGuideIds.contains(guide.getId()))
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    public void saveItinerary(TripPlan tripPlan) {
        tripItineraryRepository.save(tripPlan);
    }

    public Optional<TripPlan> getItinerary(long processKey) {
        return tripItineraryRepository.findById(processKey);
    }

    @Transactional
    public void updatePrice(long id, double price) {
        tripInformationRepository.updatePriceById(id, price);
    }

    @Transactional
    public void updateNote(long id, String note) {
        tripInformationRepository.updateNoteById(id, note);
    }

    public void sendApplication(ApplicationRequest applicationRequest) {
        String date = formatDate(Date.from(LocalDate.now().plusWeeks(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        messageService.throwMessage(
                "receive_trip_application",
                applicationRequest.getId().toString(),
                Map.of("application_id", applicationRequest.getId().toString(),
                "trip_start_date", date)

        );
    }

    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    @Transactional
    public void createTripWithCities(TripInformationDto request) {
        User coordinator = userRepository.findById(request.getCoordinatorId())
                .orElseThrow(() -> new RuntimeException(""));

        TripInformation trip = new TripInformation();
        trip.setId(request.getId());
        trip.setTripName(request.getTripName());
        trip.setCoordinator(coordinator);
        trip.setMaxTravelers(request.getMaxTravelers());
        trip.setMinTravelers(request.getMinTravelers());
        trip.setTripStartDate(request.getTripStartDate());
        trip.setTripEndDate(request.getTripEndDate());
        trip.setTransportation(request.getTransportation());

        tripInformationRepository.save(trip);

        for (TripCityDTO tripCityDTO : request.getCities()) {
            City city = cityRepository.findById(tripCityDTO.getCityId())
                    .orElseThrow(() -> new RuntimeException(""));

            TripCity tripCity = new TripCity(
                    new TripCityId(trip.getId(), city.getId()),
                    trip,
                    city,
                    tripCityDTO.getDaysSpent(),
                    request.getCities().indexOf(tripCityDTO)
            );

            tripCityRepository.save(tripCity);
        }

    }

    public TripInformationDto getTrip(Long id) {
        TripInformation trip = tripInformationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(""));

        return mapToDto(trip);
    }

    private TripInformationDto mapToDto(TripInformation trip) {
        List<TripCityDTO> cities = trip.getCities().stream()
                .map(tc -> new TripCityDTO(tc.getCity().getId(), tc.getDaysSpent(), tc.getCity().getName(), tc.getOrderInTrip(), tc.getPlan(), tc.getIncludedActivities(), tc.getExtraActivities()))
                .toList();

        return new TripInformationDto(
                trip.getId(),
                trip.getTripName(),
                trip.getTripStartDate(),
                trip.getTripEndDate(),
                trip.getMinTravelers(),
                trip.getMaxTravelers(),
                trip.getTransportation(),
                trip.getCoordinator().getId(),
                cities
        );
    }

}
