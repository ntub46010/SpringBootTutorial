package com.vincent.demo;

import com.vincent.demo.entity.app_user.UserAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum TestUser {
    ANDY("Andy_Lin", Arrays.asList(UserAuthority.ADMIN, UserAuthority.NORMAL)),
    BARBIE("Barbie_Wang", Collections.singletonList(UserAuthority.NORMAL));

    private String fullName;
    private List<UserAuthority> authorities;

    TestUser(String fullName, List<UserAuthority> authorities) {
        this.fullName = fullName;
        this.authorities = authorities;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmailAddress() {
        return fullName + "@test.com";
    }

    public List<UserAuthority> getAuthorities() {
        return authorities;
    }
}
