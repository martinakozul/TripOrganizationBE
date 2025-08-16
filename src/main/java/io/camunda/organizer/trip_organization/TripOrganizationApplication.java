package io.camunda.organizer.trip_organization;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = {
		"classpath*:/bpmn/**/*.bpmn",
		"classpath*:/bpmn/**/*.dmn",
		"classpath*:/bpmn/**/*.form"
})public class TripOrganizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripOrganizationApplication.class, args);
	}

}
