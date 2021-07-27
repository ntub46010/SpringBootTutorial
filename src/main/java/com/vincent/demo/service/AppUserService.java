package com.vincent.demo.service;

import com.vincent.demo.aop.ActionType;
import com.vincent.demo.aop.EntityType;
import com.vincent.demo.aop.SendEmail;
import com.vincent.demo.converter.AppUserConverter;
import com.vincent.demo.entity.app_user.AppUser;
import com.vincent.demo.entity.app_user.AppUserRequest;
import com.vincent.demo.entity.app_user.AppUserResponse;
import com.vincent.demo.entity.app_user.UserAuthority;
import com.vincent.demo.exception.NotFoundException;
import com.vincent.demo.exception.UnprocessableEntityException;
import com.vincent.demo.repository.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppUserService {

    private AppUserRepository repository;

    private BCryptPasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @SendEmail(entity = EntityType.APP_USER, action = ActionType.CREATE)
    public AppUserResponse createUser(AppUserRequest request) {
        Optional<AppUser> existingUser = repository.findByEmailAddress(request.getEmailAddress());
        if (existingUser.isPresent()) {
            throw new UnprocessableEntityException("This email address has been used.");
        }

        AppUser user = AppUserConverter.toAppUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        repository.insert(user);
        return AppUserConverter.toAppUserResponse(user);
    }

    public AppUserResponse getUserResponseById(String id) {
        AppUser user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find user."));

        return AppUserConverter.toAppUserResponse(user);
    }

    public AppUser getUserByEmail(String email) {
        return repository.findByEmailAddress(email)
                .orElseThrow(() -> new NotFoundException("Can't find user."));
    }

    public List<AppUserResponse> getUserResponses(List<UserAuthority> authorities) {
        if (authorities == null || authorities.isEmpty()) {
            authorities = Arrays.stream(UserAuthority.values())
                    .collect(Collectors.toList());
        }

        List<AppUser> users = repository.findByAuthoritiesIn(authorities);
        return AppUserConverter.toAppUserResponses(users);
    }

}
