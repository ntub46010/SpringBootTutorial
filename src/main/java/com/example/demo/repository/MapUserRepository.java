package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MapUserRepository implements IUserRepository {
    private static final Map<String, User> userMap = new HashMap<>();

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