package io.camunda.organizer.trip_organization.service;


import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class MessageService {

    @Autowired
    private ZeebeClient zeebeClient;

    public void throwMessage(String messageName, String correlationKey, Map<String, Object> variables) {
        PublishMessageResponse response = zeebeClient
                .newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(correlationKey)
                .variables(variables)
                .timeToLive(Duration.ofMinutes(5))
                .send()
                .join();

        System.out.println("Message published with key: " + response.getMessageKey());
    }
}