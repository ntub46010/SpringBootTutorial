package com.vincent.demo.service;

import com.vincent.demo.log.QueryHistoryService;
import com.vincent.demo.model.LikePO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LikeService {
    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);

    private final Set<LikePO> likeDB = new HashSet<>();

    @Autowired
    private UserService userService;

    @Autowired
    private QueryHistoryService historyService;

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "likeUserNames", key = "#postId"),
                    @CacheEvict(cacheNames = "likeCount", key = "#p1")
            }
    )
    public void createLike(String userId, String postId) {
        var like = LikePO.of(userId, postId);
        likeDB.add(like);
    }

    @Cacheable(cacheNames = "likeCount", key = "#postId")
    public long getLikeCount(String postId) {
        logNotReadFromCache("likeCount", postId);
        return likeDB.stream()
                .filter(like -> like.getPostId().equals(postId))
                .count();
    }

    @Cacheable(cacheNames = "likeUserNames", key = "#postId")
    public List<String> getLikeUserNames(String postId) {
        logNotReadFromCache("likeUserNames", postId);

        var userIds = likeDB.stream()
                .filter(like -> like.getPostId().equals(postId))
                .map(LikePO::getUserId)
                .toList();
        var names = userIds.stream()
                .map(uid -> userService.getUser(uid).getName())
                .toList();
        return new ArrayList<>(names);
    }

    public void deleteAll() {
        likeDB.clear();
    }

    private void logNotReadFromCache(String type, String identifier) {
        logger.info("Not read data from cache: {}-{}", type, identifier);
        historyService.add(type, identifier);
    }
}
