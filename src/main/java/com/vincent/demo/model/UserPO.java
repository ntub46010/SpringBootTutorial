package com.vincent.demo.model;

import java.io.Serializable;

public class UserPO implements Serializable {
    private String id;
    private String name;

    public static UserPO of(String id, String name) {
        var user = new UserPO();
        user.id = id;
        user.name = name;

        return user;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
