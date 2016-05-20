package com.gtp.tradeapp.service.email;

public interface EmailService {

    void sendEmail(String subject, String body, String emailTo);
}
