package com.vincent.demo.service;

import com.vincent.demo.model.LikePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LikeService {
    private final Set<LikePO> likeDB = new HashSet<>();

    @Autowired
    private UserService userService;

    public void createLike(String userId, String postId) {
        var like = LikePO.of(userId, postId);
        likeDB.add(like);
    }

    public long getLikeCount(String postId) {
        return likeDB.stream()
                .filter(like -> like.getPostId().equals(postId))
                .count();
    }

    public List<String> getLikeUserNames(String postId) {
        var userIds = likeDB.stream()
                .filter(like -> like.getPostId().equals(postId))
                .map(LikePO::getUserId)
                .toList();
        var userIdToNameMap = userService.getUserIdToNameMap(userIds);
        var names = userIds.stream()
                .map(userIdToNameMap::get)
                .toList();
        return new ArrayList<>(names);
    }

    public void deleteAll() {
        likeDB.clear();
    }
}
