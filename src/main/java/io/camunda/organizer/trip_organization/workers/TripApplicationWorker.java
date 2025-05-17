package io.camunda.organizer.trip_organization.workers;

import io.camunda.organizer.trip_organization.service.MessageService;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
public class TripApplicationWorker {

    @Autowired
    private MessageService messageService;


    private final static Logger LOG = LoggerFactory.getLogger(TripApplicationWorker.class);

    @JobWorker(type = "register_application")
    public void registerApplication(@Variable(name = "application_id") String applicationId) {
        LOG.info("application registering");
    }

    @JobWorker(type = "check_applications")
    public void checkApplications() {
        LOG.info("checking applications");
    }

    @JobWorker(type = "send_trip_application")
    public void sendApplication(@Variable(name = "application_id") String applicationId) {
        messageService.throwMessage(
                "receive_trip_application",
                applicationId,
                Map.of("application_id", applicationId)

        );
        LOG.info("sending application");
    }

    @JobWorker(type = "send_payment_proof")
    public void sendPaymentProof(@Variable(name = "application_id") String applicationId) {
        messageService.throwMessage(
                "receive_payment_proof",
                applicationId,
                Map.of()

        );
        LOG.info("no free spots");
    }

    @JobWorker(type = "send_no_free_spots")
    public void noFreeSpots(@Variable(name = "application_id") String applicationId) {
        messageService.throwMessage(
                "no_free_spots",
                applicationId,
                Map.of()

        );
        LOG.info("no free spots");
    }

    @JobWorker(type = "send_application_lock")
    public void sendApplicationLock(@Variable(name = "application_id") String applicationId) {
        messageService.throwMessage(
                "receive_application_lock",
                applicationId,
                Map.of()

        );
        LOG.info("sending lock");
    }

    @JobWorker(type = "send_cancel_application")
    public void sendCancelApplication(@Variable(name = "application_id") String applicationId) {
        messageService.throwMessage(
                "receive_cancel_application",
                applicationId,
                Map.of()

        );
        LOG.info("sending cancel");
    }

    @JobWorker(type = "send_reminder")
    public void sendReminder() {
        LOG.info("sending reminder");
    }

    @JobWorker(type = "send_warning")
    public void sendWarning() {
        LOG.info("sending warning");
    }

    @JobWorker(type = "refund_money")
    public void refundMoney() {
        LOG.info("refunding");
    }

    @JobWorker(type = "delete_application")
    public void deleteApplication() {
        LOG.info("deleting application");
    }
}