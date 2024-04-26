package fei.stu.billing.app.service.mail;


import fei.stu.billing.domain.Customer;
import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(Customer customer, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customer.email());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendEmailToMe(String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("9a.krivosudsky@gmail.com");
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
