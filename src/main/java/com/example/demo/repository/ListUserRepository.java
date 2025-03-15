package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ListUserRepository implements IUserRepository {
    private static final List<User> userList = new ArrayList<>();

    public void insert(User user) {
        var isExisting = userList
                .stream()
                .anyMatch(u -> u.getId().equals(user.getId()));
        if (isExisting) {
            throw new RuntimeException("User id " + user.getId() + " is existing.");
        }

        userList.add(user);
    }

    public User findById(String id) {
        return userList
                .stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void deleteById(String id) {
        userList.removeIf(u -> u.getId().equals(id));
    }
}