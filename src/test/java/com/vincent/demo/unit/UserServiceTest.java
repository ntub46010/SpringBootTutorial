package com.vincent.demo.unit;

import com.vincent.demo.auth.SpringUser;
import com.vincent.demo.auth.SpringUserService;
import com.vincent.demo.entity.app_user.AppUser;
import com.vincent.demo.exception.NotFoundException;
import com.vincent.demo.service.AppUserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private AppUserService appUserService;

    @InjectMocks
    private SpringUserService springUserService;

    @Test
    public void testLoadSpringUser() {
        String email = "vincent@gmail.com";
        AppUser appUser = new AppUser();
        appUser.setId("123");
        appUser.setEmailAddress(email);
        appUser.setName("Vincent Zheng");

        when(appUserService.getUserByEmail(email))
                .thenReturn(appUser);

        SpringUser springUser = (SpringUser) springUserService.loadUserByUsername(email);

        Assert.assertEquals(appUser.getId(), springUser.getId());
        Assert.assertEquals(appUser.getName(), springUser.getName());
        Assert.assertEquals(appUser.getEmailAddress(), springUser.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadSpringUserButNotFound() {
        when(appUserService.getUserByEmail(anyString()))
                .thenThrow(new NotFoundException());

        springUserService.loadUserByUsername("vincent@gmail.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAuthoritiesAsNull() {
        AppUser user = mock(AppUser.class);
        doThrow(new IllegalArgumentException())
                .when(user).setAuthorities(isNull());

        user.setAuthorities(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAuthoritiesAsEmpty() {
        AppUser user = mock(AppUser.class);
        doThrow(new IllegalArgumentException())
                .when(user).setAuthorities(Collections.emptyList());

        user.setAuthorities(new ArrayList<>());
    }
}
