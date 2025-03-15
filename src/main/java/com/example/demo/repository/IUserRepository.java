package com.example.demo.repository;

import com.example.demo.model.User;

public interface IUserRepository {
    void insert(User user);
    User findById(String id);
    void deleteById(String id);
}