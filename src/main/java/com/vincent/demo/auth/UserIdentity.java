package com.vincent.demo.auth;

import com.vincent.demo.entity.app_user.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserIdentity {

    private final SpringUser EMPTY_USER = new SpringUser(new AppUser());

    private SpringUser getSpringUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return EMPTY_USER;
        }

        return (SpringUser) authentication.getPrincipal();
    }

    public String getId() {
        return getSpringUser().getId();
    }

    public String getName() {
        return getSpringUser().getName();
    }

    public String getEmail() {
        return getSpringUser().getUsername();
    }
}
