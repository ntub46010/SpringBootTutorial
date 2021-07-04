package com.vincent.demo.config;

import com.vincent.demo.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Value("${mail.platform}")
    private String platform;
    @Value("${mail.auth.enabled}")
    private boolean authEnabled;

    @Value("${mail.starttls.enabled}")
    private boolean starttlsEnabled;

    @Value("${mail.protocol}")
    private String protocol;

    // region Gmail config
    @Value("${mail.gmail.host}")
    private String gmailHost;

    @Value("${mail.gmail.port}")
    private int gmailPort;

    @Value("${mail.gmail.username}")
    private String gmailUsername;

    @Value("${mail.gmail.password}")
    private String gmailPassword;
    // endregion Gmail

    // region Yahoo Mail config
    @Value("${mail.yahoo.host}")
    private String yahooHost;

    @Value("${mail.yahoo.port}")
    private int yahooPort;

    @Value("${mail.yahoo.username}")
    private String yahooUsername;

    @Value("${mail.yahoo.password}")
    private String yahooPassword;
    // endregion Yahoo Mail

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MailService mailService() {
        JavaMailSenderImpl mailSender = "gmail".equals(platform)
                ? gmailSender()
                : yahooSender();

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", authEnabled);
        props.put("mail.smtp.starttls.enable", starttlsEnabled);
        props.put("mail.transport.protocol", protocol);

        return new MailService(mailSender);
    }

    private JavaMailSenderImpl gmailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(gmailHost);
        mailSender.setPort(gmailPort);
        mailSender.setUsername(gmailUsername);
        mailSender.setPassword(gmailPassword);

        return mailSender;
    }

    private JavaMailSenderImpl yahooSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(yahooHost);
        mailSender.setPort(yahooPort);
        mailSender.setUsername(yahooUsername);
        mailSender.setPassword(yahooPassword);

        return mailSender;
    }
}
