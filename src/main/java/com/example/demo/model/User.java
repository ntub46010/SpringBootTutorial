package com.example.demo.model;

public class User {
    private String id;
    private String name;

    public static User of(String id, String name) {
        var u = new User();
        u.id = id;
        u.name = name;

        return u;
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
}