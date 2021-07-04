package com.vincent.demo.service;

import com.vincent.demo.auth.UserIdentity;
import com.vincent.demo.entity.mail.SendMailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Collections;
import java.util.List;

public class MailService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final JavaMailSenderImpl mailSender;
    private final UserIdentity userIdentity;

    public MailService(JavaMailSenderImpl mailSender, UserIdentity userIdentity) {
        this.mailSender = mailSender;
        this.userIdentity = userIdentity;
    }

    public void sendMail(SendMailRequest request) {
        sendMail(request.getSubject(), request.getContent(), request.getReceivers());
    }

    public void sendMail(String subject, String content, List<String> receivers) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailSender.getUsername());
        message.setTo(receivers.toArray(new String[0]));
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public void sendNewProductMail(String productId) {
        String content = String.format("Hi, %s. There's a new created product (%s).",
                userIdentity.getName(), productId);
        sendMail("New Product", content,
                Collections.singletonList(userIdentity.getEmail()));
    }

    public void sendDeleteProductMail(String productId) {
        String content = String.format("Hi, %s. There's a product deleted (%s).",
                userIdentity.getName(), productId);
        sendMail("Product Deleted", content,
                Collections.singletonList(userIdentity.getEmail()));
    }
}
