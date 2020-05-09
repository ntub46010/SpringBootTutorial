package com.vincent.demo.config;

import com.vincent.demo.service.MailService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.InternetAddress;
import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "mail")
@PropertySource("classpath:mail.properties")
public class MailConfig {

    private String gmailHost;
    private int gmailPort;
    private String gmailUserAddress;
    private String gmailUserPwd;

    private String yahooHost;
    private int yahooPort;
    private String yahooUserAddress;
    private String yahooUserPwd;

    private boolean authEnabled;
    private boolean starttlsEnabled;
    private String userDisplayName;

    private String platform;

    @Bean
    public MailService mailService() throws Exception {
        System.out.println("Create mail service.");
        return "yahoo".equals(platform)
                ? yahooMailService()
                : gmailService();
    }

    private MailService gmailService() throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", gmailHost);
        props.put("mail.smtp.port", gmailPort);
        props.put("mail.smtp.auth", String.valueOf(authEnabled));
        props.put("mail.smtp.starttls.enable",
                String.valueOf(starttlsEnabled));

        InternetAddress fromAddress =
                new InternetAddress(gmailUserAddress, userDisplayName);

        PasswordAuthentication pwdAuth =
                new PasswordAuthentication(gmailUserAddress, gmailUserPwd);

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return pwdAuth;
            }
        };

        return new MailService(props, fromAddress, authenticator);
    }

    private MailService yahooMailService() throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", yahooHost);
        props.put("mail.smtp.port", yahooPort);
        props.put("mail.smtp.auth", String.valueOf(authEnabled));
        props.put("mail.smtp.starttls.enable",
                String.valueOf(starttlsEnabled));

        InternetAddress fromAddress =
                new InternetAddress(yahooUserAddress, userDisplayName);

        PasswordAuthentication pwdAuth =
                new PasswordAuthentication(yahooUserAddress, yahooUserPwd);

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return pwdAuth;
            }
        };

        return new MailService(props, fromAddress, authenticator);
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setGmailHost(String gmailHost) {
        this.gmailHost = gmailHost;
    }

    public void setGmailPort(int gmailPort) {
        this.gmailPort = gmailPort;
    }

    public void setGmailUserAddress(String gmailUserAddress) {
        this.gmailUserAddress = gmailUserAddress;
    }

    public void setGmailUserPwd(String gmailUserPwd) {
        this.gmailUserPwd = gmailUserPwd;
    }

    public void setYahooHost(String yahooHost) {
        this.yahooHost = yahooHost;
    }

    public void setYahooPort(int yahooPort) {
        this.yahooPort = yahooPort;
    }

    public void setYahooUserAddress(String yahooUserAddress) {
        this.yahooUserAddress = yahooUserAddress;
    }

    public void setYahooUserPwd(String yahooUserPwd) {
        this.yahooUserPwd = yahooUserPwd;
    }

    public void setAuthEnabled(boolean authEnabled) {
        this.authEnabled = authEnabled;
    }

    public void setStarttlsEnabled(boolean starttlsEnabled) {
        this.starttlsEnabled = starttlsEnabled;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }
}
