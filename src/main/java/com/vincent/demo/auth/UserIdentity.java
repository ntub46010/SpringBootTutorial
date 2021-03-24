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
        Object principal = authentication.getPrincipal();
        return principal.equals("anonymousUser")
                ? EMPTY_USER
                : (SpringUser) principal;
    }

    public boolean isAnonymous() {
        return EMPTY_USER.equals(getSpringUser());
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
