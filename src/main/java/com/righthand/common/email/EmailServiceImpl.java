package com.righthand.common.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
public class EmailServiceImpl implements EmailService{

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void sendMessage(String to, String subject, String text) {

        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);
//            messageHelper.setFrom(new InternetAddress("danny@todos.co.kr", "운영자"));
            messageHelper.setFrom("no-reply@righthand.co.kr", "운영자");
            messageHelper.setTo(new InternetAddress(to, "utf-8"));
            emailSender.send(message);
        } catch (Exception e) {
            log.error("[MessageService][Error]" + e.toString());
        }
    }
}