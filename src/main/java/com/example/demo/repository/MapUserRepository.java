package com.example.demo.repository;

import com.example.demo.model.User;

import java.util.Map;

public class MapUserRepository implements IUserRepository {
    private final Map<String, User> userMap;

    public MapUserRepository(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    public void insert(User user) {
        if (userMap.containsKey(user.getId())) {
            throw new RuntimeException("User id " + user.getId() + " is existing.");
        }

        userMap.put(user.getId(), user);
    }

    public User findById(String id) {
        return userMap.get(id);
    }

    public void deleteById(String id) {
        userMap.remove(id);
    }
}