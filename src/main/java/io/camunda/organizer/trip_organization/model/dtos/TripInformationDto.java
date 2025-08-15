package io.camunda.organizer.trip_organization.model.dtos;

import io.camunda.organizer.trip_organization.model.TransportationType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.camunda.organizer.trip_organization.helper.JsonUtils.createNameValue;
import static io.camunda.organizer.trip_organization.helper.JsonUtils.getJsonBody;

public class TripInformationDto {
    private Long id;
    private String tripName;
    private Long tripStartDate;
    private Long tripEndDate;
    private int minTravelers;
    private int maxTravelers;
    private TransportationType transportation;
    private String coordinatorId;
    private List<TripCityDTO> cities;

    public TripInformationDto(Long id, String tripName, Long tripStartDate, Long tripEndDate, int minTravelers, int maxTravelers, TransportationType transportation, String coordinatorId, List<TripCityDTO> cities) {
        this.id = id;
        this.tripName = tripName;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
        this.minTravelers = minTravelers;
        this.maxTravelers = maxTravelers;
        this.transportation = transportation;
        this.coordinatorId = coordinatorId;
        this.cities = cities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public Long getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartDate(Long tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public Long getTripEndDate() {
        return tripEndDate;
    }

    public void setTripEndDate(Long tripEndDate) {
        this.tripEndDate = tripEndDate;
    }

    public int getMinTravelers() {
        return minTravelers;
    }

    public void setMinTravelers(int minTravelers) {
        this.minTravelers = minTravelers;
    }

    public int getMaxTravelers() {
        return maxTravelers;
    }

    public void setMaxTravelers(int maxTravelers) {
        this.maxTravelers = maxTravelers;
    }

    public TransportationType getTransportation() {
        return transportation;
    }

    public void setTransportation(TransportationType transportation) {
        this.transportation = transportation;
    }

    public List<TripCityDTO> getCities() {
        return cities;
    }

    public void setCities(List<TripCityDTO> cities) {
        this.cities = cities;
    }

    public String getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(String coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public String getJson(long tripId) {
        String cityData = String.join(", ", getCities().stream()
                .map(tc -> tc.getCityId().toString())
                .toList());
        List<Map<String, Object>> variables = new ArrayList<>();
        variables.add(createNameValue("trip_name", "\"" + tripName + "\""));
        variables.add(createNameValue("cities", "\"" + cityData + "\""));
        variables.add(createNameValue("trip_start_date", "\"" + formatDate(new Date(tripStartDate)) + "\""));
        variables.add(createNameValue("trip_end_date", "\"" + formatDate(new Date(tripEndDate)) + "\""));
        variables.add(createNameValue("min_travelers", "\"" + minTravelers + "\""));
        variables.add(createNameValue("max_travelers", "\"" + maxTravelers + "\""));
        variables.add(createNameValue("transportation", "\"" + transportation.name().toLowerCase() + "\""));
        variables.add(createNameValue("trip_id", "\"" + tripId + "\""));

        return getJsonBody(variables);
    }



    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }
}
