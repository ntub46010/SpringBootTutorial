package com.vincent.demo.service;

import com.vincent.demo.entity.SendMailRequest;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailService {
    private Properties props;
    private InternetAddress fromAddress;
    private Authenticator authenticator;

    public MailService(Properties props, InternetAddress fromAddress,
                       Authenticator authenticator) {
        this.props = props;
        this.fromAddress = fromAddress;
        this.authenticator = authenticator;
    }

    public void sendMail(SendMailRequest request) {
        Session session = Session.getInstance(props, authenticator);

        try {
            Message message = new MimeMessage(session);
            message.setSubject(request.getSubject());
            message.setContent(request.getContent(), "text/html; charset=UTF-8");
            message.setFrom(fromAddress);
            for (String address : request.getReceivers()) {
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(address));
            }

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
