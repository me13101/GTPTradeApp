package com.gtp.tradeapp.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(String subject, String body, String emailTo) {
        //Todo: To configure per Environment
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(emailTo);
        email.setSubject(subject);
        email.setText(body);
        //Todo: To extract this
        email.setFrom("plutos.gtp2015@gmail.com");

        mailSender.send(email);
    }
}
