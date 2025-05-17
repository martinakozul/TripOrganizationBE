package io.camunda.organizer.trip_organization.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    public static Map<String, Object> createNameValue(String name, Object value) {
        Map<String, Object> variable = new HashMap<>();
        variable.put("name", name);
        variable.put("value", value instanceof String ? value : String.valueOf(value));
        return variable;
    }

    public static Map<String, Object> createValueType(Object value, String type) {
        Map<String, Object> variable = new HashMap<>();
        variable.put("value", value instanceof String ? value : String.valueOf(value));
        variable.put("type", type);
        return variable;
    }

    public static String getJsonBody(List<Map<String, Object>> variables) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = "";
        try {
            jsonBody = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException ignored) {
        }

        return jsonBody;
    }


}
