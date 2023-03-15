package com.vincent.demo;

public class TaskCycleConfig {
    private Integer delay;
    private String cron;

    public static TaskCycleConfig of(int delay) {
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

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
