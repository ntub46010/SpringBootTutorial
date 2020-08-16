package com.vincent.demo.service;

import com.vincent.demo.converter.AppUserConverter;
import com.vincent.demo.entity.app_user.AppUser;
import com.vincent.demo.entity.app_user.AppUserRequest;
import com.vincent.demo.entity.app_user.AppUserResponse;
import com.vincent.demo.exception.ConflictException;
import com.vincent.demo.exception.NotFoundException;
import com.vincent.demo.repository.AppUserRepository;

import java.util.List;
import java.util.Optional;

public class AppUserService {

    private AppUserRepository repository;

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
    }

    public AppUserResponse createUser(AppUserRequest request) {
        Optional<AppUser> existingUser = repository.findByEmailAddress(request.getEmailAddress());
        if (existingUser.isPresent()) {
            throw new ConflictException("This email address has been used.");
        }

        AppUser user = AppUserConverter.toAppUser(request);
        user = repository.insert(user);
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

    public List<AppUserResponse> getUserResponses() {
        List<AppUser> users = repository.findAll();
        return AppUserConverter.toAppUserResponses(users);
    }

}
