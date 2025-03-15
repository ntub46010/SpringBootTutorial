package com.example.demo.repository;

import com.example.demo.model.User;

import java.util.List;

public class ListUserRepository implements IUserRepository {
    private final List<User> userList;

    public ListUserRepository(List<User> userList) {
        this.userList = userList;
    }

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