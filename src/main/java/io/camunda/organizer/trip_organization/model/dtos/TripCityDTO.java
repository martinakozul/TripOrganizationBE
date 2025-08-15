package io.camunda.organizer.trip_organization.model.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.camunda.organizer.trip_organization.helper.JsonUtils.createNameValue;
import static io.camunda.organizer.trip_organization.helper.JsonUtils.getJsonBody;

public class TripCityDTO {
    private Long cityId;
    private int daysSpent;
    private String cityName;
    private int order;
    private String plan;
    private List<String> includedActivities = new ArrayList<>();
    private List<String> extraActivities = new ArrayList<>();


    public TripCityDTO() {
    }

    public TripCityDTO(Long cityId, int daysSpent) {
        this.cityId = cityId;
        this.daysSpent = daysSpent;
    }

    public TripCityDTO(Long cityId, int daysSpent, String cityName, int order, String plan, List<String> includedActivities, List<String> extraActivities) {
        this.cityId = cityId;
        this.daysSpent = daysSpent;
        this.cityName = cityName;
        this.order = order;
        this.plan = plan;
        this.includedActivities = includedActivities;
        this.extraActivities = extraActivities;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getCityName() {
        return cityName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public int getDaysSpent() {
        return daysSpent;
    }

    public void setDaysSpent(int daysSpent) {
        this.daysSpent = daysSpent;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

    public String getJson() {
        List<Map<String, Object>> variables = new ArrayList<>();
        variables.add(createNameValue("trip_plan", "\"" + plan + "\""));
        variables.add(createNameValue("included_activities", "\"" + getCleanStringFromList(includedActivities) + "\""));
        variables.add(createNameValue("extra_activities", "\"" + getCleanStringFromList(extraActivities) + "\""));

        return getJsonBody(variables);
    }

    private String getCleanStringFromList(List<String> list) {
        if (list == null) return "";
        String clean = String.join(";", list);
        return clean.replaceAll("\\r?\\n", "");
    }
}
