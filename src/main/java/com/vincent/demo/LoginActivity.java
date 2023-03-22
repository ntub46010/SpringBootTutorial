package com.vincent.demo;

import java.util.Date;

public class LoginActivity {
    private String name;
    private Date loginTime;
    private boolean notified = false;

    public static LoginActivity of(String name, Date loginTime) {
        var activity = new LoginActivity();
        activity.name = name;
        activity.loginTime = loginTime;
        return activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }
}
