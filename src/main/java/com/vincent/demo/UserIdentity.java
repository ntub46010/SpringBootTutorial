package com.vincent.demo;

import com.vincent.demo.model.AppUser;
import com.vincent.demo.model.AppUserDetails;
import com.vincent.demo.model.UserAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserIdentity {
    private static final AppUserDetails EMPTY_USER_DETAILS = new AppUserDetails(new AppUser());

    private AppUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        return "anonymousUser".equals(principal)
                ? EMPTY_USER_DETAILS
                : (AppUserDetails) principal;
    }

    public String getId() {
        return getUserDetails().getId();
    }

    public String getUsername() {
        return getUserDetails().getUsername();
    }

    public UserAuthority getUserAuthority() {
        return getUserDetails().getUserAuthority();
    }

    public boolean isPremium() {
        return getUserDetails().isPremium();
    }
}