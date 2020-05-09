package com.vincent.demo.service;

import com.vincent.demo.entity.SendMailRequest;

import javax.annotation.PreDestroy;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MailService {
    private final Properties props;
    private final InternetAddress fromAddress;
    private final Authenticator authenticator;
    private final List<String> mailMessages;
    private final long tag;

    public MailService(Properties props, InternetAddress fromAddress,
                       Authenticator authenticator) {
        this.props = props;
        this.fromAddress = fromAddress;
        this.authenticator = authenticator;
        this.tag = System.currentTimeMillis();
        this.mailMessages = new ArrayList<>();
    }

    public void sendMail(SendMailRequest request) {
        sendMail(request.getSubject(), request.getContent(), request.getReceivers());
    }

    public void sendMail(String subject, String content, String receiver) {
        sendMail(subject, content, Collections.singletonList(receiver));
    }

    public void sendMail(String subject, String content, List<String> receivers) {
        Session session = Session.getInstance(props, authenticator);

        try {
            Message message = new MimeMessage(session);
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=UTF-8");
            message.setFrom(fromAddress);
            for (String address : receivers) {
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(address));
            }
            Transport.send(message);

            mailMessages.add(content);
            printMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNewProductMail(String productId) {
        String message = String.format("There's a new created product (%s).", productId);
        sendMail("New Product", message, "google_account@gmail.com");
    }

    public void sendDeleteProductMail(String productId) {
        String message = String.format("There's a product deleted (%s).", productId);
        sendMail("Product Deleted", message, "google_account@gmail.com");
    }

    private void printMessages() {
        System.out.println("----------");
        mailMessages.forEach(System.out::println);
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("##########");
        System.out.printf("Spring Boot is about to destroy Mail Service %d.\n\n", tag);
    }

}
