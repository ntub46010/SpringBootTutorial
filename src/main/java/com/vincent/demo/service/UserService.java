package com.vincent.demo.service;

import com.vincent.demo.model.UserPO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, UserPO> userDB = new HashMap<>();

    public UserPO createUser(String id, String name) {
        var user = UserPO.of(id, name);
        userDB.put(user.getId(), user);
        return user;
    }

    public void updateUser(UserPO user) {
        userDB.put(user.getId(), user);
    }

    public UserPO getUser(String id) {
        return userDB.get(id);
    }

    public void deleteAll() {
        userDB.clear();
    }
}
