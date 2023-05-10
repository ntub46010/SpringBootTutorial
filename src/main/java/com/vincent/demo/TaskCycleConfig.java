package com.vincent.demo;

public class TaskCycleConfig {
    private Integer delay;
    private Integer rate;
    private String cron;

    public static TaskCycleConfig ofDelay(int delay) {
        var config = new TaskCycleConfig();
        config.delay = delay;

        return config;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
