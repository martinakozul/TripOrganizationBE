package io.camunda.organizer.trip_organization.workers;

import io.camunda.organizer.trip_organization.helper.CamundaLogHelper;
import io.camunda.organizer.trip_organization.model.database.ApplicationRequest;
import io.camunda.organizer.trip_organization.service.EmailService;
import io.camunda.organizer.trip_organization.service.MessageService;
import io.camunda.organizer.trip_organization.service.TripApplicationService;
import io.camunda.organizer.trip_organization.service.TripService;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

@Component
public class TripApplicationWorker {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TripService tripService;

    @Autowired
    private TripApplicationService tripApplicationService;

    @Autowired
    private EmailService emailService;

    @JobWorker(type = "register_application")
    public Map<String, Boolean> registerApplication(@Variable(name = "application_id") String applicationId) {
        return Map.of(
                "no_space", tripApplicationService.deleteIfTripFull(Long.valueOf(applicationId))
        );
    }

    @JobWorker(type = "check_applications")
    public Map<String, Boolean>  checkApplications(@Variable(name = "trip_id") String tripId) {
        CamundaLogHelper.logToCsvPrep(Long.parseLong(tripId), "Check application count", null, "backend");

        return Map.of(
                "enough_applications", tripApplicationService.hasEnoughApplications(Long.valueOf(tripId))
        );
    }

    @JobWorker(type = "send_trip_application")
    public void sendApplication(@Variable(name = "application_id") String applicationId) {
        messageService.throwMessage(
                "receive_trip_application",
                applicationId,
                Map.of("application_id", applicationId)

        );
    }

    @JobWorker(type = "send_payment_proof")
    public void sendPaymentProof(@Variable(name = "application_id") String applicationId) {
        tripApplicationService.markAsPaid(Long.valueOf(applicationId));
        messageService.throwMessage(
                "receive_payment_proof",
                applicationId,
                Map.of()
        );
    }

    @JobWorker(type = "send_no_available_spots")
    public void noFreeSpots(@Variable(name = "application_id") String applicationId) {
        CamundaLogHelper.logToCsvApplication(Long.parseLong(applicationId), "Send available spots", null, "User");

        messageService.throwMessage(
                "no_free_spots",
                applicationId,
                Map.of()

        );
        emailService.sendTestEmail(
                "",
                "automated.travels@gmail.com",
                "Application request response",
                "We regret to inform you we cannot confirm your reservation due to the lack of free spots available."
        );
    }

    @JobWorker(type = "send_application_lock")
    public void sendApplicationLock(@Variable(name = "application_id") String applicationId) {
        messageService.throwMessage(
                "receive_application_lock",
                applicationId,
                Map.of()

        );
    }

    @JobWorker(type = "send_cancel_application")
    public void sendCancelApplication(@Variable(name = "application_id") String applicationId) {
        messageService.throwMessage(
                "receive_cancel_application",
                applicationId,
                Map.of()

        );
        emailService.sendTestEmail(
                "automated.travels@gmail.com",
                "",
                "Cancel application request",
                "Cancel application with id " + applicationId
        );
    }

    @JobWorker(type = "send_reminder")
    public void sendReminder(@Variable(name = "application_id") String applicationId) {
        CamundaLogHelper.logToCsvApplication(Long.parseLong(applicationId), "Reminder deadline ended", null, "User");


        CamundaLogHelper.logToCsvApplication(Long.parseLong(applicationId), "Send payment reminder",LocalDateTime.now().plusSeconds(5).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), "User");

        emailService.sendTestEmail(
                "",
                "automated.travels@gmail.com",
                "Payment reminder",
                "We wish to remind you your application still hasn't been finalized due to lack of payment."
        );
    }

    @JobWorker(type = "send_warning")
    public void sendWarning(@Variable(name = "application_id") String applicationId) {
        CamundaLogHelper.logToCsvApplication(Long.parseLong(applicationId), "Warning deadline ended", null, "User");

        CamundaLogHelper.logToCsvApplication(Long.parseLong(applicationId), "Send payment warning",  LocalDateTime.now().plusSeconds(5).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), "User");

        emailService.sendTestEmail(
                "automated.travels@gmail.com",
                "",
                "Payment reminder",
                "We wish to remind you your application still hasn't been finalized due to lack of payment. In case your application is not paid in the next 24 hours, we will have to cancel your reservation."
        );
    }

    @JobWorker(type = "refund_money")
    public void refundMoney(@Variable(name = "application_id") String applicationId) {
        CamundaLogHelper.logToCsvApplication(Long.parseLong(applicationId), "Get refund percentage", null, "User");

        CamundaLogHelper.logToCsvApplication(Long.parseLong(applicationId), "Refund money", null, "User");

        emailService.sendTestEmail(
                "automated.travels@gmail.com",
                "",
                "Application request response",
                "We're sorry to see you go! You can expect the refund in your bank account in the next week. The amound refunded is calculated based on the distance from the departure date. Find the exact terms attached below."
        );
    }

    @JobWorker(type = "delete_application")
    public void deleteApplication(@Variable(name = "application_id") String applicationId) {
        tripApplicationService.deleteExpiredApplication(Long.valueOf(applicationId));
        CamundaLogHelper.logToCsvApplication(Long.parseLong(applicationId), "Delete application", null, "User");
    }
}