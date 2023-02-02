package com.vincent.demo.model;

public class User {
    private String id;
    private String name;
    private String email;

    public static User of(String id, String name, String email) {
        var user = new User();
        user.id = id;
        user.name = name;
        user.email = email;
        return user;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
