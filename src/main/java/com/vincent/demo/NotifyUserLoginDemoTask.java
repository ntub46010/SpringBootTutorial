package com.vincent.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class NotifyUserLoginDemoTask implements SchedulingConfigurer {
    private final Logger logger = LoggerFactory.getLogger(NotifyUserLoginDemoTask.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm:ss");
    private int count = 1;
    private TaskCycleConfig config = TaskCycleConfig.ofDelay(15000);

    @Value("${task.notify_login_user.mode}")
    private String mode;

    @Value("${task.notify_login_user.cron_exp}")
    private String cronExp;

    @Value("${task.notify_login_user.interval}")
    private long interval;

    @Value("${task.notify_login_user.initial_delay}")
    private long initialDelay;

    @Autowired
    private LoginActivityRepository repository;

    @Scheduled(initialDelay = 20000, fixedDelay = 5000)
    public void createLoginData() {
        var name = "User_" + count;
        repository.insert(name);

        logger.info("執行 createLoginData: {}", count);
        count++;
    }

    public void notifyLoginUser() {
        logger.info("開始發送登入通知");

        var activities = repository.findByNotNotified();
        activities.forEach(act -> {
            var timeStr = sdf.format(act.getLoginTime());
            logger.info("親愛的 {}，您於 {} 登入本服務。", act.getName(), timeStr);

            act.setNotified(true);
        });
    }

    public void setNotifyConfig(TaskCycleConfig config) {
        this.config = config;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        Runnable runnable = this::notifyLoginUser;

        Trigger trigger = new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                if (config.getCron() != null) {
                    return new CronTrigger(config.getCron()).nextExecutionTime(triggerContext);
                } else if (config.getDelay() != null) {
                    var t = new PeriodicTrigger(config.getDelay(), TimeUnit.MILLISECONDS);
                    t.setFixedRate(false);
                    return t.nextExecutionTime(triggerContext);
                } else if (config.getRate() != null) {
                    var t = new PeriodicTrigger(config.getRate(), TimeUnit.MILLISECONDS);
                    t.setFixedRate(true);
                    return t.nextExecutionTime(triggerContext);
                } else {
                    throw new RuntimeException("Please define parameters for scheduled task.");
                }
            }
        };

        registrar.addTriggerTask(runnable, trigger);
    }
}
