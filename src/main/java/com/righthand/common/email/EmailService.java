package com.righthand.common.email;

import org.springframework.mail.MailException;

public interface EmailService {
    public void sendMessage(String to, String subject, String text) throws MailException;
}
