package com.vincent.demo;

import com.vincent.demo.model.AppUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Repository
public class UserRepository {
    private final Map<String, AppUser> idToUserMap = new HashMap<>();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init() {
        var adminUser = AppUser.getTestAdminUser();
        var normalUser = AppUser.getTestNormalUser();
        var guestUser = AppUser.getTestGuestUser();

        Stream.of(adminUser, normalUser, guestUser)
                .forEach(u -> {
                    var encodedPwd = passwordEncoder.encode(u.getPassword());
                    u.setPassword(encodedPwd);
                });

        idToUserMap.put(adminUser.getId(), adminUser);
        idToUserMap.put(normalUser.getId(), normalUser);
        idToUserMap.put(guestUser.getId(), guestUser);
    }

    public AppUser findById(String id) {
        return idToUserMap.get(id);
    }

    public AppUser findByEmail(String email) {
        return idToUserMap.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public List<AppUser> findAll() {
        return new ArrayList<>(idToUserMap.values());
    }

    public void insert(AppUser user) {
        var encodedPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPwd);
        idToUserMap.put(user.getId(), user);
    }
}