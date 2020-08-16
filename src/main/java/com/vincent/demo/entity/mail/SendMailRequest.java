package com.vincent.demo.entity.mail;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class SendMailRequest {

    @NotEmpty
    private String subject;

    @NotEmpty
    private String content;

    @NotEmpty
    private List<String> receivers;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }
}
