package io.camunda.organizer.trip_organization.workers;

import io.camunda.organizer.trip_organization.service.MessageService;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TripPaymentWorker {
    private final static Logger LOG = LoggerFactory.getLogger(TripApplicationWorker.class);

    @Autowired
    private MessageService messageService;

    @JobWorker(type = "send_invoice")
    public void sendInvoice(@Variable(name = "application_id") String applicationId) {
        messageService.throwMessage(
                "send_invoice",
                applicationId,
                Map.of()

        );
        LOG.info("sending invoice");
    }

    @JobWorker(type = "send_invoice_expired")
    public void sendInvoiceExpired(@Variable(name = "application_id") String applicationId) {
        messageService.throwMessage(
                "receive_invoice_expired",
                applicationId,
                Map.of()

        );
        LOG.info("sending invoice expired");
    }

    @JobWorker(type = "send_reminder")
    public void sendReminder() {
        LOG.info("sending reminder");
    }

    @JobWorker(type = "send_warning")
    public void sendWarning() {
        LOG.info("sending warning");
    }

    @JobWorker(type = "cancel_accommodation_and_transport")
    public void cancelAccommodationAndTransport() {
        LOG.info("canceling partners");
    }

    @JobWorker(type = "pay_partners")
    public void payAccommodationAndTransport() {
        LOG.info("paying partners");
    }

    @JobWorker(type = "refund_applicants")
    public void refundApplicants() {
        LOG.info("refunding applicants");
    }
}
