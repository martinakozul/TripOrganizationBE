package io.camunda.organizer.trip_organization.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.organizer.trip_organization.model.database.City;
import io.camunda.organizer.trip_organization.model.dtos.BasicTrip;
import io.camunda.organizer.trip_organization.model.database.TripInformation;
import io.camunda.organizer.trip_organization.model.database.TripPlan;
import io.camunda.organizer.trip_organization.model.dtos.TripInformationDto;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.camunda.organizer.trip_organization.helper.JsonUtils.createNameValue;
import static io.camunda.organizer.trip_organization.helper.JsonUtils.getJsonBody;

@RestController
public class TaskListController {

    private final RestTemplate restTemplate = new RestTemplate();

    public void getCreateTripForm(long processDefinitionKey) {
        String url = "http://localhost:8082/v1/forms/create_trip";

        String urlWithParams = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("processDefinitionKey", processDefinitionKey)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getBearerToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String response = restTemplate.exchange(urlWithParams, HttpMethod.GET, entity, String.class).getBody();

        System.out.println("Response: " + response);
    }

    public List<BasicTrip> getActiveTasksForUser(String username) {
        String url = "http://localhost:8082/v1/tasks/search";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getBearerToken());

        HttpEntity<String> entity = new HttpEntity<>(String.format(
                """
                        {
                          "state": "CREATED",
                          "assignee": "%s"
                        }"""
                , username), headers);

        String response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();

        System.out.println("Response: " + response);

        ObjectMapper objectMapper = new ObjectMapper();
        List<BasicTrip> basicTripInfo = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    JsonNode nameNode = node.get("name");
                    JsonNode processKeyNode = node.get("processInstanceKey");
                    if (nameNode != null && nameNode.isTextual()) {
                        basicTripInfo.add(new BasicTrip(processKeyNode.asLong(), nameNode.asText()));
                    }
                }
            }
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }

        System.out.println("Names: " + basicTripInfo);
        return basicTripInfo;

    }

    public String getTaskId(long processInstanceKey) {
        String url = "http://localhost:8082/v1/tasks/search";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getBearerToken());

        HttpEntity<String> entity = new HttpEntity<>(String.format(
                """
                        {
                          "processInstanceKey": "%d"
                        }"""
                , processInstanceKey), headers);

        String response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();

        System.out.println("Response: " + response);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            return "";
        }

        String taskId = rootNode.get(0).get("id").asText();

        System.out.println("Extracted ID: " + taskId);

        return taskId;
    }

    public void completeTripCreation(TripInformationDto tripInformation, long processInstanceKey) {
        String url = "http://localhost:8082/v1/tasks/" + getTaskId(processInstanceKey) + "/complete";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getBearerToken());

        HttpEntity<String> requestEntity = new HttpEntity<>(tripInformation.getJson(processInstanceKey), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class
            );
            System.out.println("Response: " + response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error Response: " + e.getResponseBodyAsString());
        }
    }

    public void reviewItinerary(Long processKey, Double price, String note) {
        String url = "http://localhost:8082/v1/tasks/" + getTaskId(processKey) + "/complete";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getBearerToken());

        List<Map<String, Object>> variables = new ArrayList<>();
        if (note != null) {
            variables.add(createNameValue("note", "\"" + note + "\""));
            variables.add(createNameValue("approved", "false"));
        } else {
            variables.add(createNameValue("price", price));
            variables.add(createNameValue("approved", "true"));
        }


        HttpEntity<String> requestEntity = new HttpEntity<>(getJsonBody(variables), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class
            );
            System.out.println("Response: " + response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error Response: " + e.getResponseBodyAsString());
        }
    }

    public void assignTourGuide(String tourGuide, long processKey) {
        String url = "http://localhost:8082/v1/tasks/" + getTaskId(processKey) + "/complete";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getBearerToken());

        List<Map<String, Object>> variables = new ArrayList<>();
        variables.add(createNameValue("tour_guide", "\"" + tourGuide + "\""));

        HttpEntity<String> requestEntity = new HttpEntity<>(getJsonBody(variables), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class
            );
            System.out.println("Response: " + response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error Response: " + e.getResponseBodyAsString());
        }
    }

    public void fillTripPlan(TripPlan tripPlan, String taskId) {
        String url = "http://localhost:8082/v1/tasks/" + taskId + "/complete";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getBearerToken());

        HttpEntity<String> requestEntity = new HttpEntity<>(tripPlan.getJson(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class
            );
            System.out.println("Response: " + response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error Response: " + e.getResponseBodyAsString());
        }
    }

    public void reviewPartnerOffers(Long transportPartnerId, Long accommodationTransportId, String taskId) {
        String url = "http://localhost:8082/v1/tasks/" + taskId + "/complete";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getBearerToken());

        List<Map<String, Object>> variables = new ArrayList<>();
        if (transportPartnerId == null || accommodationTransportId == null) {
            variables.add(createNameValue("reject_all", "true"));
        } else {
            variables.add(createNameValue("accommodation_id", "\"" + accommodationTransportId + "\""));
            variables.add(createNameValue("transport_id", "\"" + transportPartnerId + "\""));
            variables.add(createNameValue("reject_all", "false"));
        }

        String json = getJsonBody(variables);

        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class
            );
            System.out.println("Response: " + response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error Response: " + e.getResponseBodyAsString());
        }
    }

//    public void applyForTheTrip(ApplicationRequest request, String taskId) {
//        String url = "http://localhost:8082/v1/tasks/" + taskId + "/complete";
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(getBearerToken());
//
//        HttpEntity<String> requestEntity = new HttpEntity<>(request.getJson(), headers);
//
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.PATCH,
//                    requestEntity,
//                    String.class
//            );
//            System.out.println("Response: " + response.getBody());
//        } catch (HttpClientErrorException | HttpServerErrorException e) {
//            System.err.println("Error Response: " + e.getResponseBodyAsString());
//        }
//    }

//    public void getTripData(long taskId) {
//        String url = "http://localhost:8082/v1/task/"+taskId;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + getBearerToken());
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
//
//        System.out.println("Response: " + response);
//    }

    private String getBearerToken() {
        String url = "http://localhost:18080/auth/realms/camunda-platform/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String formData = "client_id=trip-organization&client_secret=LxNjhIb2KcKlJj9oS6a7i9snoO9BcrVr&grant_type=client_credentials";

        HttpEntity<String> entity = new HttpEntity<>(formData, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        System.out.println("Response: " + response.getBody());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            String accessToken = responseJson.get("access_token").asText();

            System.out.println("Extracted Access Token: " + accessToken);

            return accessToken;
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
