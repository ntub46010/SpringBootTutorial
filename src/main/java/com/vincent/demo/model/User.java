package com.vincent.demo.model;

import java.util.Date;

public class User {
    private String id;
    private String name;
    private String username;
    private String email;
    private Date createdTime;

    public User() {
    }

    public User(String id, String name, String username, String email, Date createdTime) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
