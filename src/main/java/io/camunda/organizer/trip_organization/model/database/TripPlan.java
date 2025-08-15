package io.camunda.organizer.trip_organization.model.database;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.camunda.organizer.trip_organization.helper.JsonUtils.createNameValue;
import static io.camunda.organizer.trip_organization.helper.JsonUtils.getJsonBody;

@Entity
@Table(name = "trip_itinerary")
public class TripPlan {

    @Id
    long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonAlias("trip_plan")
    private List<String> tripPlan;
    @JsonAlias("included_activities")
    private List<String> includedActivities;
    @JsonAlias("extra_activities")
    private List<String> extraActivities;
    @JsonAlias("note")
    private String note;

    public TripPlan() {
    }

    public TripPlan(long id, List<String> tripPlan, List<String> includedActivities, List<String> extraActivities, String note) {
        this.id = id;
        this.tripPlan = tripPlan;
        this.includedActivities = includedActivities;
        this.extraActivities = extraActivities;
        this.note = note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getTripPlan() {
        return tripPlan;
    }

    public void setTripPlan(List<String> tripPlan) {
        this.tripPlan = tripPlan;
    }

    public List<String> getIncludedActivities() {
        return includedActivities;
    }

    public void setIncludedActivities(List<String> includedActivities) {
        this.includedActivities = includedActivities;
    }

    public List<String> getExtraActivities() {
        return extraActivities;
    }

    public void setExtraActivities(List<String> extraActivities) {
        this.extraActivities = extraActivities;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getJson() {
        List<Map<String, Object>> variables = new ArrayList<>();
        variables.add(createNameValue("trip_plan", "\"" + getCleanStringFromList(tripPlan) + "\""));
        variables.add(createNameValue("included_activities", "\"" + getCleanStringFromList(includedActivities) + "\""));
        variables.add(createNameValue("extra_activities", "\"" + getCleanStringFromList(extraActivities) + "\""));
        variables.add(createNameValue("note", "\"" + note + "\""));

        return getJsonBody(variables);
    }

    private String getCleanStringFromList(List<String> list) {
        String clean = String.join(";", list);
        return clean.replaceAll("\\r?\\n", "");
    }
}
