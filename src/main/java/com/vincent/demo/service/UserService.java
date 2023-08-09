package com.vincent.demo.service;

import com.vincent.demo.log.QueryHistoryService;
import com.vincent.demo.model.UserPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final Map<String, UserPO> userDB = new HashMap<>();

    @Autowired
    private QueryHistoryService historyService;

    @CachePut(cacheNames = "user", key = "#id", condition = "#saveCache")
    public UserPO createUser(String id, String name, boolean saveCache) {
        var user = UserPO.of(id, name);
        userDB.put(user.getId(), user);
        return user;
    }

    @CachePut(cacheNames = "user", key = "#user.id")
    public UserPO updateUser(UserPO user) {
        userDB.put(user.getId(), user);
        return user;
    }

    @Cacheable(cacheNames = "user", key = "#p0", unless = "#result.id.startsWith('test-')")
    public UserPO getUser(String id) {
        logNotReadFromCache("user", id);
        return userDB.get(id);
    }

    public void deleteAll() {
        userDB.clear();
    }

    private void logNotReadFromCache(String type, String identifier) {
        logger.info("Not read data from cache: {}-{}", type, identifier);
        historyService.add(type, identifier);
    }
}
