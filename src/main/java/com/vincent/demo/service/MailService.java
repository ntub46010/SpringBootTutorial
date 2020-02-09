package com.vincent.demo.service;

import com.vincent.demo.config.MailConfig;
import com.vincent.demo.entity.SendMailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@Service
public class MailService {

    @Autowired
    private MailConfig mailConfig;

    public void sendMail(SendMailRequest request) {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailConfig.getHost());
        props.put("mail.smtp.port", mailConfig.getPort());
        props.put("mail.smtp.auth", String.valueOf(mailConfig.isAuthEnabled()));
        props.put("mail.smtp.starttls.enable",
                String.valueOf(mailConfig.isStarttlsEnabled()));

        Session session = Session.getInstance(props, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(
                        mailConfig.getUserAddress(), mailConfig.getUserPwd());
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setSubject(request.getSubject());
            message.setContent(request.getContent(), "text/html; charset=UTF-8");
            message.setFrom(new InternetAddress(
                    mailConfig.getUserAddress(), mailConfig.getUserDisplayName()));
            for (String address : request.getReceivers()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
            }

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
