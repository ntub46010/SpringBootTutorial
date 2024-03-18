package com.example.demo.repository;

import com.example.demo.model.UserPO;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Repository
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

    public List<UserPO> getManyByIds(Collection<String> ids) {
        return userMap.values()
                .stream()
                .filter(u -> ids.contains(u.getId()))
                .toList();
    }
}
