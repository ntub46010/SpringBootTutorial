package com.example.demo.repository;

import com.example.demo.model.UserPO;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class UserRepository {
    private static final Map<String, UserPO> userMap = new HashMap<>();

    static {
        Stream.of(
                UserPO.of("U1", "Vincent"),
                UserPO.of("U2", "Ivy")
        ).forEach(u -> userMap.put(u.getId(), u));
    }

    public UserPO getOneById(String id) {
        return userMap.get(id);
    }
}
