package com.vincent.demo.model;

import java.util.Objects;

public class LikePO {
    private String userId;
    private String postId;

    public static LikePO of(String userId, String postId) {
        var like = new LikePO();
        like.userId = userId;
        like.postId = postId;

        return like;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var other = (LikePO) o;
        return Objects.equals(userId, other.getUserId()) &&
                Objects.equals(postId, other.getPostId());
    }
}
