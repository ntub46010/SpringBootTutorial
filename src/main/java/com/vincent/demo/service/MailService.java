package com.vincent.demo.service;

import com.vincent.demo.entity.SendMailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Service
public class MailService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 587;
    private static final boolean ENABLE_AUTH = true;
    private static final boolean ENABLE_STARTTLS = true;
    private static final String PROTOCOL = "smtp";
    private static final String USERNAME = "your_email_address@gmail.com";
    private static final String PASSWORD = "your_email_password";
    private JavaMailSenderImpl mailSender;

    @PostConstruct
    private void init() {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(HOST);
        mailSender.setPort(PORT);
        mailSender.setUsername(USERNAME);
        mailSender.setPassword(PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", ENABLE_AUTH);
        props.put("mail.smtp.starttls.enable", ENABLE_STARTTLS);
        props.put("mail.transport.protocol", PROTOCOL);
    }

    public void sendMail(SendMailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(USERNAME);
        message.setTo(request.getReceivers());
        message.setSubject(request.getSubject());
        message.setText(request.getContent());

        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
