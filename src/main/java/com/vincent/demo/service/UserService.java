package com.vincent.demo.service;

import com.vincent.demo.model.UserPO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public Map<String, String> getUserIdToNameMap(Collection<String> ids) {
        // In real project, you may have something like "UserRepository.findByIdIn(ids)". It just needs 1 DB operation.
        return ids.stream()
                .map(userDB::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserPO::getId, UserPO::getName));
    }

    public void deleteAll() {
        userDB.clear();
    }
}
