package com.vincent.demo.model;

import java.time.LocalDate;

public class AppUser {
    private String id;
    private String email;
    private String password;
    private UserAuthority authority;
    private boolean enabled = true;
    private boolean premium = false;
    private LocalDate trailExpiration;
    private String creatorId;

    public static AppUser getTestAdminUser() {
        var user = new AppUser();
        user.id = "100";
        user.email = "vincent@gmail.com";
        user.password = "123456";
        user.authority = UserAuthority.ADMIN;

        return user;
    }

    public static AppUser getTestNormalUser() {
        var user = new AppUser();
        user.id = "101";
        user.email = "dora@gmail.com";
        user.password = "654321";
        user.authority = UserAuthority.NORMAL;

        return user;
    }

    public static AppUser getTestGuestUser() {
        var user = new AppUser();
        user.id = "000";
        user.email = "ivy@gmail.com";
        user.password = "000000";
        user.authority = UserAuthority.GUEST;
        user.enabled = false;
        user.trailExpiration = LocalDate.of(2022, 12, 31);

        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserAuthority getAuthority() {
        return authority;
    }

    public void setAuthority(UserAuthority authority) {
        this.authority = authority;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public LocalDate getTrailExpiration() {
        return trailExpiration;
    }

    public void setTrailExpiration(LocalDate trailExpiration) {
        this.trailExpiration = trailExpiration;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
