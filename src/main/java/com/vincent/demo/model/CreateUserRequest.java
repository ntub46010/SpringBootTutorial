package com.vincent.demo.model;

public class CreateUserRequest {
    private String name;
    private String job;

    public static CreateUserRequest of(String name, String job) {
        var req = new CreateUserRequest();
        req.name = name;
        req.job = job;
        return req;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
