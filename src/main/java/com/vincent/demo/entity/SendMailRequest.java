package com.vincent.demo.entity;

import javax.validation.constraints.NotEmpty;

public class SendMailRequest {

    @NotEmpty
    private String subject;

    @NotEmpty
    private String content;

    @NotEmpty
    private String[] receivers;

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

    public String[] getReceivers() {
        return receivers;
    }

    public void setReceivers(String[] receivers) {
        this.receivers = receivers;
    }
}
