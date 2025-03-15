package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.repository.IUserRepository;
import com.example.demo.repository.ListUserRepository;
import com.example.demo.repository.MapUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;

@Configuration
public class RepositoryConfig {

    @Bean
    public IUserRepository userRepository(
            @Value("${user-repository.storage}") String storage,
            @Value("${user-repository.test-data.amount:0}") int amount
    ) {
        // 建立測試資料
        var users = new ArrayList<User>();
        for (var i = 1; i <= amount; i++) {
            var user = User.of("U" + i, "Test User " + i);
            users.add(user);
        }

        // 選擇資料結構
        if ("map".equalsIgnoreCase(storage)) {
            var userMap = new HashMap<String, User>();
            users.forEach(u -> userMap.put(u.getId(), u));
            return new MapUserRepository(userMap);
        } else if ("list".equalsIgnoreCase(storage)) {
            return new ListUserRepository(users);
        } else {
            throw new IllegalArgumentException("Please provide correct user repository storage type.");
        }
    }
}