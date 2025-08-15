package io.camunda.organizer.trip_organization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTestEmail(String to, String from, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        if (to.isEmpty()) {
            to = "to@gmail.com";
        }
        if (from.isEmpty()) {
            from = "from@gmail.com";
        }
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(from);

//        mailSender.send(message);
    }
}

