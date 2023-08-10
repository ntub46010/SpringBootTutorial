package com.vincent.demo.model;

import java.util.Date;

public class UserPO {
    private String id;
    private String name;
    private Date createTime;

    public static UserPO of(String id, String name) {
        var user = new UserPO();
        user.id = id;
        user.name = name;
        user.createTime = new Date();

        return user;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
