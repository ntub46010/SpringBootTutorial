package com.vincent.demo.service;

import com.vincent.demo.UserRepository;
import com.vincent.demo.model.AppUser;
import com.vincent.demo.model.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByEmail(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("Can't find user: " + username);
        }

        return new AppUserDetails(appUser);
    }
}