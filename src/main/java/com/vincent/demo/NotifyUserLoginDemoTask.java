package com.vincent.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.*;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"squid:S106"})
@Component
public class NotifyUserLoginDemoTask implements SchedulingConfigurer {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    private int count = 1;

    // default value from DB or property file
    private TaskCycleConfig config = TaskCycleConfig.of(15000);

    @Autowired
    private LoginActivityRepository repository;

    @Scheduled(initialDelay = 20000, fixedDelay = 5000)
    public void createLoginData() {
        var name = "User_" + count;
        repository.insert(name);

        System.out.printf("%s 登入%n", name);
        count++;
    }

    public void notifyLoginUser() {
        System.out.printf("開始發送登入通知 %s%n", sdf.format(new Date()));

        var activities = repository.findByNotNotified();
        activities.forEach(act -> {
            var msg = generateMessage(act);
            System.out.println(msg);

            act.setNotified(true);
        });
    }

    private String generateMessage(LoginActivity activity) {
        var name = activity.getName();
        var timeStr = sdf.format(activity.getLoginTime());

        return String.format("%s 您好！您於 %s 登入本服務。", name, timeStr);
    }

    public void setNotifyConfig(TaskCycleConfig config) {
        this.config = config;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        Runnable runnable = this::notifyLoginUser;

        Trigger trigger = triggerContext -> {
            if (config.getDelay() != null) {
                return new PeriodicTrigger(config.getDelay(), TimeUnit.MILLISECONDS).nextExecutionTime(triggerContext);
            } else if (config.getCron() != null) {
                return new CronTrigger(config.getCron()).nextExecutionTime(triggerContext);
            } else {
                throw new RuntimeException("Scheduled task isn't configured correctly.");
            }
        };

        registrar.addTriggerTask(new TriggerTask(runnable, trigger));
    }
}
