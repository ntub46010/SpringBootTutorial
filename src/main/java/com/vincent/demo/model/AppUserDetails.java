package com.vincent.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class AppUserDetails implements UserDetails {
    private final AppUser appUser;

    public AppUserDetails(AppUser user) {
        this.appUser = user;
    }

    public String getUsername() {
        return appUser.getEmail();
    }

    public String getPassword() {
        return appUser.getPassword();
    }

    public boolean isEnabled() {
        return appUser.isEnabled();
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(appUser.getAuthority());
    }

    public boolean isAccountNonExpired() {
        if (appUser.getTrailExpiration() == null) {
            return true;
        }

        return LocalDate.now().isBefore(appUser.getTrailExpiration());
    }

    public String getId() {
        return appUser.getId();
    }

    public UserAuthority getUserAuthority() {
        return appUser.getAuthority();
    }

    public boolean isPremium() {
        return appUser.isPremium();
    }

    public LocalDate getTrailExpiration() {
        return appUser.getTrailExpiration();
    }
}