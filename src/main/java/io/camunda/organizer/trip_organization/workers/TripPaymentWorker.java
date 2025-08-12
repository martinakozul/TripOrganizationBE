package io.camunda.organizer.trip_organization.workers;

import io.camunda.organizer.trip_organization.helper.CamundaLogHelper;
import io.camunda.organizer.trip_organization.service.EmailService;
import io.camunda.organizer.trip_organization.service.MessageService;
import io.camunda.organizer.trip_organization.service.TripApplicationService;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TripPaymentWorker {
    @Autowired
    private MessageService messageService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TripApplicationService tripApplicationService;

    @JobWorker(type = "send_invoice")
    public void sendInvoice(@Variable(name = "application_id") String applicationId) {
        CamundaLogHelper.logToCsvApplication(Long.parseLong(applicationId), "Send invoice", null, "User");

        messageService.throwMessage(
                "receive_invoice",
                applicationId,
                Map.of()

        );
        emailService.sendTestEmail("",
                "automated.travels@gmail.com",
                "Spot reserved",
                "To confirm your spot on the trip, please pay the invoice attached below in the next 3 days.");
    }

    @JobWorker(type = "send_invoice_expired")
    public void sendInvoiceExpired(@Variable(name = "application_id") String applicationId) {
        tripApplicationService.deleteExpiredApplication(Long.valueOf(applicationId));

        CamundaLogHelper.logToCsvApplication(Long.parseLong(applicationId), "Send error invoice not paid", null, "User");

        CamundaLogHelper.logToCsvApplication(Long.parseLong(applicationId), "Send invoice expired", null, "User");

        emailService.sendTestEmail("",
                "automated.travels@gmail.com",
                "Reservation expired",
                "We are sorry to inform you we have not received payment in time and therefor we had to cancel your reservation.");
        messageService.throwMessage(
                "receive_invoice_expired",
                applicationId,
                Map.of()

        );
    }

    @JobWorker(type = "cancel_partners")
    public void cancelAccommodationAndTransport(@Variable(name = "trip_id") String tripId) {
        CamundaLogHelper.logToCsvPrep(Long.parseLong(tripId), "Cancel accommodation and transport", null, "Backend");
        emailService.sendTestEmail("",
                "automated.travels@gmail.com",
                "Offer response",
                "Sadly due to lack of applications, we have to cancel the trip and the reservation made with you. We thank you for your offer and we hope to work together soon.");

    }

    @JobWorker(type = "pay_partners")
    public void payAccommodationAndTransport(@Variable(name = "trip_id") String tripId) {
        CamundaLogHelper.logToCsvPrep(Long.parseLong(tripId), "Pay partners", null, "Backend");

        emailService.sendTestEmail("",
                "automated.travels@gmail.com",
                "Reservation confirmation",
                "");
    }

    @JobWorker(type = "refund_applicants")
    public void refundApplicants(@Variable(name = "trip_id") String tripId) {
        CamundaLogHelper.logToCsvPrep(Long.parseLong(tripId), "Refund applicants", null, "Backend");
        emailService.sendTestEmail("",
                "automated.travels@gmail.com",
                "Trip cancellation",
                "");
    }
}
