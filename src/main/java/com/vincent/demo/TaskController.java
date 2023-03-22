package com.vincent.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/task", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

    @Autowired
    private NotifyUserLoginDemoTask notifyUserLoginTask;

    @PostMapping("/notifyUserLogin")
    public ResponseEntity<Void> setNotifyUserLoginTaskConfig(@RequestBody TaskCycleConfig config) {
        notifyUserLoginTask.setNotifyConfig(config);
        return ResponseEntity.noContent().build();
    }
}
