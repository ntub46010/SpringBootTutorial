package com.vincent.demo.model;

public class PostPO {
    private String id;
    private String creatorId;
    private String title;

    public static PostPO of(String id, String creatorId, String title) {
        var post = new PostPO();
        post.id = id;
        post.creatorId = creatorId;
        post.title = title;

        return post;
    }

    public String getId() {
        return id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getTitle() {
        return title;
    }
}
